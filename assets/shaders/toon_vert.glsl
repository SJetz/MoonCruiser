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
    vec2 texture;
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

void main(){
    mat4 modelview = view_matrix * model_matrix;
    vec4 viewpos = modelview * vec4(position, 1.0f);
    vertexData.toCamera = -viewpos.xyz;

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