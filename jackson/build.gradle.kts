dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":utils"))

    api("com.fasterxml.jackson.core:jackson-core:2.21.2") // Jackson
    api("com.fasterxml.jackson.core:jackson-annotations:2.21") // Jackson
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.21.1") // Jackson
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.1") // Jackson (DataTypes)
}