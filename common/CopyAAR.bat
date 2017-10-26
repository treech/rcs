
del ..\home\libs\common-release.aar /f /q
del ..\message\libs\common-release.aar /f /q
del ..\contacts\libs\common-release.aar /f /q
del ..\phone\libs\common-release.aar /f /q
del ..\me\libs\common-release.aar /f /q
del ..\oa\libs\common-release.aar /f /q

xCopy build\outputs\aar\common-release.aar ..\home\libs\ /y /e
xCopy build\outputs\aar\common-release.aar ..\message\libs\ /y /e
xCopy build\outputs\aar\common-release.aar ..\contacts\libs\ /y /e
xCopy build\outputs\aar\common-release.aar ..\phone\libs\ /y /e
xCopy build\outputs\aar\common-release.aar ..\me\libs\ /y /e
xCopy build\outputs\aar\common-release.aar ..\oa\libs\ /y /e
exit