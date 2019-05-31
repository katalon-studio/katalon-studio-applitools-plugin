# Katalon Studio Applitools Plugin

## Develop

```
./gradlew clean katalonCopyDependencies
```

Create file /settings/external/com.kms.katalon.keyword.Applitools-Keywords:

```properties
#Tue Apr 09 09:27:20 ICT 2019
Visual\ Grid\ View\ Port=""
API\ Key="<Applitools API Key>"
Hide\ Caret="false"
Application\ Name="Katalon Studio"
Match\ Level="STRICT"
```

## Package

Make sure to open this project using Katalon Studio at least once.

```
./gradlew clean katalonPluginPackage
```

## License

Copyright (c) Katalon LLC. All rights reserved.

Licensed under the LICENSE AGREEMENT FOR KATALON AUTOMATION FRAMEWORK.
