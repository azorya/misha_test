# misha_test
1.  [get java 11 for your platform](https://www.azul.com/downloads/zulu-community/?architecture=x86-64-bit&package=jdk)
2.  [get gradle](https://gradle.org/install/)
3.  clone this repo
4.  set JAVA_HOME environment variable to the place wehre you unpack java

example:

    alex@ubuntus1:~/apps/java_versions$ ls -l
    total 4
    lrwxrwxrwx  1 alex alex   36 Jan 17 09:24 current -> zulu11.37.17-ca-jdk11.0.6-linux_x64/
    drwxrwxr-x 10 alex alex 4096 Jan 17 09:23 zulu11.37.17-ca-jdk11.0.6-linux_x64
    alex@ubuntus1:~/apps/java_versions$ 

    alex@ubuntus1:~/apps/java_versions$ cd ..
    alex@ubuntus1:~/apps$ ls -l
    total 28
    lrwxrwxrwx 1 alex alex   23 Mar 28 16:10 gradle -> gradle_versions/current
    drwxr-xr-x 3 alex alex 4096 Mar 28 16:10 gradle_versions
    lrwxrwxrwx 1 alex alex   23 Jan 17 09:25 java -> ./java_versions/current
    drwxr-xr-x 3 alex alex 4096 Jan 17 09:24 java_versions
In this example you want to set JAVA_HOME=~/apps/java

run the following commands:

    alex@ubuntus1:~/misha/misha_test$ gradle wrapper

    BUILD SUCCESSFUL in 609ms
    1 actionable task: 1 executed
   
    alex@ubuntus1:~/misha/misha_test$ ls
    build.gradle  gradle  gradle.properties  gradlew  gradlew.bat  README.md  settings.gradle  zSchemas  zSDump
    alex@ubuntus1:~/misha/misha_test$ ./gradlew build

you should see the output like this one:

    > Task :zSchemas:compileJava
    Note: Some input files use unchecked or unsafe operations.
    Note: Recompile with -Xlint:unchecked for details.

    > Task :zSDump:compileJava
    Note: /home/alex/misha/misha_test/zSDump/src/main/java/com/zgconsultants/avro/tools/zSReg.java uses unchecked or unsafe operations.
    Note: Recompile with -Xlint:unchecked for details.

    Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
    Use '--warning-mode all' to show the individual deprecation warnings.
    See https://docs.gradle.org/6.3/userguide/command_line_interface.html#sec:command_line_warnings

now go to the ... _misha_test/zSDump/build/distributions_
