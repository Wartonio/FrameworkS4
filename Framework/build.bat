set miditra=cd
set mivoka=cd..
set chemin1=.\Framework\
set package=./etu001935
set name_jar=framework
set chemin2="../Test framework/lib"

%miditra% %chemin1%
javac -d . *.java
jar -cf %name_jar%.jar %package%
xcopy %name_jar%.jar %chemin2%





@REM javac -d . *.java
@REM jar -cf framework.jar ./etu001935
@REM xcopy framework.jar "./Test framework/web/WEB-INF/lib"
@REM javac -cp "./web/WEB-INF/lib/framework.jar" -d "./web/WEB-INF/classes" ./test/*.java
@REM cd ./web
@REM jar -cf projetTest.war 
@REM xcopy ".\web\projetTest.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0_Tomcat10.7\webapps"







@REM set chemin1=Framework/
@REM set chemin2=../Test framework/web/WEB-INF/lib
@REM set chemin3=../Test framework/web/WEB-INF/lib/framework.jar
@REM set chemin4=../Test framework/web/WEB-INF/classes
@REM set chemin5=../Test framework/web
@REM set chemin6=.\web\projetTest.war
@REM set chemin7=C:\Program Files\Apache Software Foundation\Tomcat 10.0_Tomcat10.7\webapps


@REM javac -d . %chemin1% *.java
@REM jar -cf framework.jar ./etu001935
@REM xcopy framework.jar %chemin2%
@REM mkdir temp