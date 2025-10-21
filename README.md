## ️ Requirements

- Oracle OpenJDK  **24.0.1**
- javaFX SDK **24.0.1**

> [!NOTE]
> This app was developed with Oracle OpenJDK 24.0.1 and javaFX 24.0.1 **and is only tested with that version**
> Compatibility with other java/javaFX versions is unknown

> [!IMPORTANT]
> JavaFX should be installed according to the instructions for non-modular from the javaFX website [here](https://openjfx.io/openjfx-docs/#install-javafx)
> with the following vm options 
> for Linux/macOS
```
--module-path /path/to/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml
```
Or the following for Windows
```
--module-path "\path\to\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml
```
## Movie/Series Posters
Movie/Series posters are not included, they are stored under [```resources/movies/```](https://github.com/georgemax026/java-project-public/tree/main/src/main/resources/images/movies)
and [```resources/series/```](https://github.com/georgemax026/java-project-public/tree/main/src/main/resources/images/series)\
If you wish to add posters, put them in their respective dirs formated like the following:
```mX.jpg``` for movies, or
```sX.jpg``` for series.\
```X``` represents the ID of the movie/series that will be associated with the poster image.

## Contributing

This repository will not be accepting contributions (pull requests, issues, etc.).\
If you wish to create changes to the source code and encourage collaboration, please create a fork of the repository under your GitHub user/organization space.