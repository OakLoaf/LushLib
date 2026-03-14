dependencies {
    compileOnly(project(":utils"))

    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0") // Jackson
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20") // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.20.0") // Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.0") // Jackson (DataTypes)
}