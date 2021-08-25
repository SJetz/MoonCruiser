#version 330 core

//input from vertex shader
in struct VertexData
{
    vec2 textureCoordinate;
    vec3 normal;
    //light
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
    vec3 toBikeSpotLight;
    vec3 toBikeSpotLight2;
    //camera
    vec3 toCamera;
} vertexData;

uniform sampler2D materialDiff;
uniform sampler2D materialSpec;
uniform sampler2D materialEmit;
uniform float materialShininess;

uniform vec3 shadingColor = vec3(1f,1f,1f);

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

    vec3 Lbsl = normalize(vertexData.toBikeSpotLight);
    vec3 Lbsl2 = normalize(vertexData.toBikeSpotLight2);
    vec3 V = normalize(vertexData.toCamera);

    vec3 diffColor = texture(materialDiff, vertexData.textureCoordinate).rgb;
    vec3 specColor = texture(materialSpec, vertexData.textureCoordinate).rgb;
    vec3 emitColor = texture(materialEmit, vertexData.textureCoordinate).rgb;

    vec3 emit_term = emitColor * shadingColor;

    vec3 intSpotLight = getSpotLightIntensity(bikeSpotLightColor, vertexData.toBikeSpotLight, bikeSpotLightDirection, bikeSpotLightCone);
    vec3 intSpotLight2 = getSpotLightIntensity(bikeSpotLight2Color, vertexData.toBikeSpotLight2, bikeSpotLight2Direction, bikeSpotLight2Cone);

    // Blinn-Phong shading:
    vec3 sshade =  shadeBlinn(N, Lbsl, V, diffColor, specColor, materialShininess);
    vec3 sshade2 =  shadeBlinn(N, Lbsl2, V, diffColor, specColor, materialShininess);

    //texture mit spotlight
    vec4 precolor = vec4(emit_term +
    sshade * intSpotLight +
    sshade2 * intSpotLight2, 1.0f);

    //lichteinflüsse
    color = (vertexData.tocolor + vertexData.toambientcolor ) * vertexData.tocolor * precolor;



}
