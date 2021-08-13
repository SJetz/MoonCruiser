#version 330 core

//input from vertex shader
out struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
//light stuff
    vec3 toCamera;
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

} vertexData;

uniform sampler2D materialDiff;
uniform sampler2D materialSpec;
uniform sampler2D materialEmit;
uniform float materialShininess;

uniform vec3 shadingColor;

//lights


//pixel shader output
out vec4 color;


float getAttenuationFactor(float distance, vec3 attParams){
    return 1.0f / (attParams.x + attParams.y * distance + attParams.z * distance * distance);
}

vec3 getPointLightIntensity(vec3 color, vec3 toLightVector, vec3 attParams)
{
    float lth = length(toLightVector);
    return color * getAttenuationFactor(lth, attParams);
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

    vec3 V = normalize(vertexData.toCamera);

    vec3 diffColor = texture(materialDiff, vertexData.textureCoordinate).rgb;
    vec3 specColor = texture(materialSpec, vertexData.textureCoordinate).rgb;
    vec3 emitColor = texture(materialEmit, vertexData.textureCoordinate).rgb;

    vec3 emit_term = emitColor * shadingColor;
    vec4 texture = vec4(emit_term+diffColor,1.0);

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



    color = (light + vertexData.toambientcolor + specular + rim) * vertexData.tocolor *vec4(diffColor,1.0);
    color =texture;


}