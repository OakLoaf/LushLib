dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":utils"))

    // Libraries
    // TODO: Find out if jackson MUST be on the server to use annotations
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.20") // Jackson
}