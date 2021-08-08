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
uniform vec3 bikeSpotLightPosition;

uniform vec3 bikePointLight2Position;
uniform vec3 bikePointLight3Position;

out struct VertexData
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

void main(){
    mat4 modelview = view_matrix * model_matrix;
    vec4 viewpos = modelview * vec4(position, 1.0f);
    vertexData.toCamera = -viewpos.xyz;
    vertexData.toBikePointLight = (view_matrix * vec4(bikePointLightPosition, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikePointLight2 = (view_matrix * vec4(bikePointLight2Position, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikePointLight3 = (view_matrix * vec4(bikePointLight3Position, 1.0)).xyz - viewpos.xyz;
    vertexData.toBikeSpotLight = (view_matrix * vec4(bikeSpotLightPosition, 1.0)).xyz - viewpos.xyz;
    gl_Position = proj_matrix * viewpos;

    vertexData.normal = (inverse(transpose(modelview)) * vec4(normal, 0.0f)).xyz;
    vertexData.textureCoordinate = textureCoordinate * tcMultiplier;
}