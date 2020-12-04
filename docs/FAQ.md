## Возможные проблемы при подключении

#### При подключении через maven выводится ошибка `Error:java: error: release version 5 not supported`
Попробуйте прописать в pom.xml следующее:
~~~
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
~~~
Или попробовать решения по [ссылке](https://ru.stackoverflow.com/questions/966260/errorjava-error-release-version-5-not-supported).