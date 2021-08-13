#version 330 core

//input from vertex shader
in struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
    //light stuff
    vec3 toCamera;
    vec3 toBikePointLight;
    vec3 toBikePointLight2;
    vec3 toBikePointLight3;
    vec3 toBikeSpotLight;
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
uniform vec3 bikeSpotLightColor;
uniform vec3 bikeSpotLightDirection;
uniform vec3 bikeSpotLightAttParams;

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
    vec3 V = normalize(vertexData.toCamera);

    vec3 diffColor = texture(materialDiff, vertexData.textureCoordinate).rgb;
    vec3 specColor = texture(materialSpec, vertexData.textureCoordinate).rgb;
    vec3 emitColor = texture(materialEmit, vertexData.textureCoordinate).rgb;

    vec3 emit_term = emitColor * shadingColor;

    vec3 pshade =  shade(N, Lbpl, V, diffColor, specColor, materialShininess);
    vec3 pshade2 =  shade(N, Lbpl2, V, diffColor, specColor, materialShininess);
    vec3 pshade3 =  shade(N, Lbpl3, V, diffColor, specColor, materialShininess);

    vec3 sshade =  shade(N, Lbsl, V, diffColor, specColor, materialShininess);

    vec3 intPointLight = getPointLightIntensity(bikePointLightColor, vertexData.toBikePointLight, bikePointLightAttParams);
    vec3 intPointLight2 = getPointLightIntensity(bikePointLight2Color, vertexData.toBikePointLight2, bikePointLight2AttParams);
    vec3 intPointLight3 = getPointLightIntensity(bikePointLight3Color, vertexData.toBikePointLight3, bikePointLight3AttParams);

    vec3 intSpotLight = getSpotLightIntensity(bikeSpotLightColor, vertexData.toBikeSpotLight, bikeSpotLightDirection, bikeSpotLightCone);


    color = vec4(emit_term +
                 pshade * intPointLight +
                 pshade2 * intPointLight2 +
                 pshade3 * intPointLight3 +
                 sshade * intSpotLight, 1.0f);

    // Blinn-Phong shading:

    pshade =  shadeBlinn(N, Lbpl, V, diffColor, specColor, materialShininess*2);
    pshade2 = shadeBlinn(N, Lbpl2, V, diffColor, specColor, materialShininess*2);
    pshade3 = shadeBlinn(N, Lbpl3, V, diffColor, specColor, materialShininess*2);

    sshade =  shadeBlinn(N, Lbsl, V, diffColor, specColor, materialShininess);

    color = vec4(emit_term +
                pshade * intPointLight +
                pshade2 * intPointLight2 +
                pshade3 * intPointLight3 +
                sshade * intSpotLight, 1.0f);



}