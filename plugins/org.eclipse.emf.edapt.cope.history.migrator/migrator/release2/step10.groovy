changeClass = history.Change
getReleaseOperation = changeClass.newEOperation()
getReleaseOperation.name = "getRelease"
releaseClass = history.Release
getReleaseOperation.eType = releaseClass
