dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":utils"))

    api("com.fasterxml.jackson.core:jackson-core:2.22.0") // Jackson
    api("com.fasterxml.jackson.core:jackson-annotations:2.22") // Jackson
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.22.0") // Jackson
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.22.0") // Jackson (DataTypes)
}