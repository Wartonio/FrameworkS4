javac -d . *.java
jar -cf framework.jar ./etu001935
xcopy framework.jar "../Test framework/web/WEB-INF/lib"
