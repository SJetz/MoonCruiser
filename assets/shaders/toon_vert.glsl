#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoordinate;
layout(location = 2) in vec3 normal;

//uniforms
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;

uniform vec2 tcMultiplier;

// lights
uniform vec3 bikePointLightPosition;
uniform vec3 bikePointLight2Position;
uniform vec3 bikePointLight3Position;

uniform vec3 bikeSpotLightPosition;
uniform vec3 bikeSpotLight2Position;

uniform vec3  direction = vec3(0.2f,-0.5f,-1.0f);
uniform float intensity = 1;
uniform vec4 diffuse = vec4(1f,1f,1f,1f);
uniform vec4 color = vec4(1f,1f,1f,1f);
uniform vec4 ambientcolor = vec4(0.4f,0.4f,0.4f,1f);
uniform vec4 specularcolor = vec4(0.75f,0.75f,0.75f,1f);
uniform float glossines = 32f;
uniform vec4 rimcolor = vec4(1f,1f,1f,1f);
uniform float rimamount = 0.716f;
uniform float rimthreshhold = 0.1f;

out struct VertexData
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
//light stuff
    vec3 toCamera;
    vec3 toBikePointLight;
    vec3 toBikePointLight2;
    vec3 toBikePointLight3;
    vec3 toBikeSpotLight;
    vec3 toBikeSpotLight2;
} vertexData;

void main(){
    mat4 modelview = view_matrix * model_matrix;
    vec4 viewpos = modelview * vec4(position, 1.0f);
    vertexData.toCamera = -viewpos.xyz;

    vertexData.toBikePointLight = (view_matrix * vec4(bikePointLightPosition, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikePointLight2 = (view_matrix * vec4(bikePointLight2Position, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikePointLight3 = (view_matrix * vec4(bikePointLight3Position, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikeSpotLight = (view_matrix * vec4(bikeSpotLightPosition, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikeSpotLight2 = (view_matrix * vec4(bikeSpotLight2Position, 1.0)).xyz - viewpos.xyz;

    gl_Position = proj_matrix * viewpos;

    vertexData.normal = (inverse(transpose(modelview)) * vec4(normal, 0.0f)).xyz;
    vertexData.textureCoordinate = textureCoordinate * tcMultiplier;

    vertexData.todirection = direction;
    vertexData.tointensity = intensity;
    vertexData.todiffuse = diffuse;
    vertexData.tocolor = color;
    vertexData.toambientcolor = ambientcolor;
    vertexData.tospecularcolor = specularcolor;
    vertexData.toglossiness = glossines;
    vertexData.torimcolor = rimcolor;
    vertexData.torimamount = rimamount;
    vertexData.torimthreshhold = rimthreshhold;
}