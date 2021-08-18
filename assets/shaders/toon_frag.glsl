#version 330 core

//input from vertex shader
in struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
    //light stuff
        vec3 todirection;
        float tointensity;
        vec4 todiffuse;
        vec4 tocolor;
        vec4 toambientcolor;
        vec4 tospecularcolor;
        float toglossiness;
        vec4 torimcolor;
        float torimamount;
        float torimthreshhold;
//light stuff
    vec3 toCamera;
    vec3 toBikePointLight;
    vec3 toBikePointLight2;
    vec3 toBikePointLight3;
    vec3 toBikeSpotLight;
    vec3 toBikeSpotLight2;
} vertexData;

uniform sampler2D materialDiff;
uniform sampler2D materialSpec;
uniform sampler2D materialEmit;
uniform float materialShininess;

uniform vec3 shadingColor;

//lights
uniform vec3 bikePointLightColor;
uniform vec3 bikePointLight2Color;
uniform vec3 bikePointLight3Color;

uniform vec3 bikePointLightAttParams;
uniform vec3 bikePointLight2AttParams;
uniform vec3 bikePointLight3AttParams;

uniform vec2 bikeSpotLightCone;
uniform vec2 bikeSpotLight2Cone;

uniform vec3 bikeSpotLightColor;
uniform vec3 bikeSpotLight2Color;

uniform vec3 bikeSpotLightDirection;
uniform vec3 bikeSpotLight2Direction;

uniform vec3 bikeSpotLightAttParams;
uniform vec3 bikeSpotLight2AttParams;

//fragment shader output
out vec4 color;


float getAttenuationFactor(float distance, vec3 attParams){
    return 1.0f / (attParams.x + attParams.y * distance + attParams.z * distance * distance);
}

vec3 getPointLightIntensity(vec3 color, vec3 toLightVector, vec3 attParams)
{
    float lth = length(toLightVector);
    return color * getAttenuationFactor(lth, attParams);
}

vec3 getSpotLightIntensity(vec3 color, vec3 toLightVector, vec3 lightdir, vec2 cone)
{
    float lth = length(toLightVector);
    float cosfpos = dot(lightdir, normalize(toLightVector));
    float att = clamp((cosfpos - cos(cone.y)) / (cos(cone.x) - cos(cone.y)), 0.0f, 1.0f);
    return color * att * getAttenuationFactor(lth, bikeSpotLightAttParams);
}

vec3 shade(vec3 N, vec3 L, vec3 V, vec3 diffc, vec3 specc, float shn)
{
    vec3 R = normalize(reflect(-L, N));
    float NdotL = max(dot(N, L), 0.0f);
    float RdotV = max(dot(R, V), 0.0f);
    return diffc * NdotL + specc * pow(RdotV, shn);
}

vec3 shadeBlinn(vec3 N, vec3 L, vec3 V, vec3 diffc, vec3 specc, float shn)
{
    vec3 H = normalize(V + L);
    float NdotL = max(dot(N, L), 0.0f);
    float HdotN = max(dot(H, N), 0.0f);
    return diffc * NdotL + specc * pow(HdotN, shn);
}


void main(){

    vec3 N = normalize(vertexData.normal);
    vec3 Lbpl = normalize(vertexData.toBikePointLight);
    vec3 Lbpl2 = normalize(vertexData.toBikePointLight2);
    vec3 Lbpl3 = normalize(vertexData.toBikePointLight3);

    vec3 Lbsl = normalize(vertexData.toBikeSpotLight);
    vec3 Lbsl2 = normalize(vertexData.toBikeSpotLight2);
    vec3 V = normalize(vertexData.toCamera);

    vec3 diffColor = texture(materialDiff, vertexData.textureCoordinate).rgb;
    vec3 specColor = texture(materialSpec, vertexData.textureCoordinate).rgb;
    vec3 emitColor = texture(materialEmit, vertexData.textureCoordinate).rgb;

    vec3 emit_term = emitColor * shadingColor;

    vec3 pshade =  shade(N, Lbpl, V, diffColor, specColor, materialShininess);
    vec3 pshade2 =  shade(N, Lbpl2, V, diffColor, specColor, materialShininess);
    vec3 pshade3 =  shade(N, Lbpl3, V, diffColor, specColor, materialShininess);

    vec3 sshade =  shade(N, Lbsl, V, diffColor, specColor, materialShininess);
    vec3 sshade2 =  shade(N, Lbsl2, V, diffColor, specColor, materialShininess);

    vec3 intPointLight = getPointLightIntensity(bikePointLightColor, vertexData.toBikePointLight, bikePointLightAttParams);
    vec3 intPointLight2 = getPointLightIntensity(bikePointLight2Color, vertexData.toBikePointLight2, bikePointLight2AttParams);
    vec3 intPointLight3 = getPointLightIntensity(bikePointLight3Color, vertexData.toBikePointLight3, bikePointLight3AttParams);

    vec3 intSpotLight = getSpotLightIntensity(bikeSpotLightColor, vertexData.toBikeSpotLight, bikeSpotLightDirection, bikeSpotLightCone);
    vec3 intSpotLight2 = getSpotLightIntensity(bikeSpotLight2Color, vertexData.toBikeSpotLight2, bikeSpotLight2Direction, bikeSpotLight2Cone);

    color = vec4(emit_term +
    pshade * intPointLight +
    pshade2 * intPointLight2 +
    pshade3 * intPointLight3 +
    sshade * intSpotLight +
    sshade2 * intPointLight2, 1.0f);


    // Blinn-Phong shading:

    pshade =  shadeBlinn(N, Lbpl, V, diffColor, specColor, materialShininess*2);
    pshade2 = shadeBlinn(N, Lbpl2, V, diffColor, specColor, materialShininess*2);
    pshade3 = shadeBlinn(N, Lbpl3, V, diffColor, specColor, materialShininess*2);

    sshade =  shadeBlinn(N, Lbsl, V, diffColor, specColor, materialShininess);
    sshade2 =  shadeBlinn(N, Lbsl2, V, diffColor, specColor, materialShininess);

    vec4 precolor = vec4(emit_term +
    pshade * intPointLight +
    pshade2 * intPointLight2 +
    pshade3 * intPointLight3 +
    sshade * intSpotLight +
    sshade2 * intPointLight2, 1.0f);

    //lighting below is calcutaled using blinn phong

    //the dot product is need to get a relastic style of illumination, depnig on the litght direction
    float NdotL = dot(vertexData.todirection*-1, N);

    //the lower and upper bounce a close together in order to mainten a relatively sharp, toon edge
    //smoothstep creates a interpolation between the 2 values to avoide a jagged break
    float lightIntensity = smoothstep(0, 0.01, NdotL);
    //lightintensity is multipled with the light color, so the color makes a visible difference
    vec4 light = lightIntensity*vertexData.tocolor;

    // calculate a specular reflection
    // the strength of the specular reflection is defined in Blinn-Phong as the dot product between the normal of the surface and the half vector.
    // the half vector is a vector between the viewing direction and the light source.
    vec3 halfVector = normalize(vertexData.todirection * -1.0f + V);
    float NdotH = dot(N, halfVector);

    // the size of the specular reflection is set by this pow function
    // NdotH is multiplied by lightIntensity to endsure that the reflection is only drawn when the surface is lit.
    // glossiness is only multiplied with itslef to allow smaller values to have a larger effect.
    float specularIntensity = pow(NdotH * lightIntensity, vertexData.toglossiness * vertexData.toglossiness);
    float specularIntensitySmooth = smoothstep(0.005, 0.01, specularIntensity);
    vec4 specular = specularIntensitySmooth * vertexData.tospecularcolor;

    // rim lighting is the addition of illumination to the edges of an object to simulate reflected light or backlighting

    // the rim of an object will be defined as surfaces that are facing away from the camera
    // so I calculate the dot of the normal and the view direction and invert it
    float rimDot = 1 - dot(V, N);

    // the pow function is used to scale the rim
    float rimIntensity = rimDot * pow(NdotL, vertexData.torimthreshhold);
    // this makes that the rim will only appear on the illuminated surfaces
    rimIntensity = smoothstep(vertexData.torimamount - 0.01, vertexData.torimamount + 0.01, rimIntensity);
    vec4 rim = rimIntensity * vertexData.torimcolor;

    color = (light + vertexData.toambientcolor + specular + rim) * vertexData.tocolor * precolor;


}