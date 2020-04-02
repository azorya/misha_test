# misha_test
1.  [get java 11 for your platform](https://www.azul.com/downloads/zulu-community/?architecture=x86-64-bit&package=jdk)
2.  [get gradle](https://gradle.org/install/)
3.  set JAVA_HOME environment variable to the place wehre you unpack java 

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

