# Smith RAG module


Working Steps:

AndroidStudio -> File -> Project Structure -> Add dependency
search org.apache.poi
search com.itextpdf:itextpdf

This will update root libs.version.toml as will
or you can add this
```xml
[versions]
.....
poi = "5.4.0"
poiOoxml = "5.4.0"
itextpdf = "5.5.13.4"

[libraries]
....
poi = { group = "org.apache.poi", name = "poi", version.ref = "poi" }
poi-ooxml = { group = "org.apache.poi", name = "poi-ooxml", version.ref = "poiOoxml" }
itextpdf = { group = "com.itextpdf", name = "itextpdf", version.ref = "itextpdf" }
```