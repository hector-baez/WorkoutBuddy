apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.userAuthPresentation))

    "implementation"(Coil.coilCompose)
    "implementation"(Coil.coilSvg)
    "implementation" ("com.google.accompanist:accompanist-pager:0.23.1")

    "implementation"("co.yml:ychat:1.4.1")
}