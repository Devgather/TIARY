<div align="center">
    <img src="https://github.com/user-attachments/assets/f5f5199e-349a-4586-b5a4-566a129162c4" height="120"/>
</div>

<div align="center">

# TIARY

내가 오늘 배운 것을 작성하고 공유하는 플랫폼

[![][github-release-badge]][github-release-link]
[![][github-contributors-badge]][github-contributors-link]
[![][github-forks-badge]][github-forks-link]
[![][github-stars-badge]][github-stars-link]<br/>
[![][sonar-coverage-badge]][sonar-link]
[![][sonar-quality-gate-badge]][sonar-link]

</div>

## 시작하기

애플리케이션이 정상적으로 동작하려면 추가적인 설정이 필요하며, 관련 내용은 [Configuration](https://github.com/Devgather/TIARY/wiki/Configuration) 문서를 참고하세요. 다음 명령어를 실행하면 설정된 프로파일에 따라 애플리케이션이 실행됩니다:

``` sh
./gradlew bootRun --args='--spring.profiles.active=<profile>'
```

> [!TIP]
> local 프로파일의 경우 브라우저에서 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)에 접속하면 데이터베이스를 확인할 수 있습니다.

| 설정 항목 | 값 |
| - | - |
| Driver Class | org.h2.Driver |
| JDBC URL | jdbc:h2:mem:tiary;MODE=MYSQL |
| User Name | sa |
| Password | |

더 자세한 사용 방법은 [Wiki](https://github.com/Devgather/TIARY/wiki)에서 확인하실 수 있습니다.

[github-contributors-badge]: https://img.shields.io/github/contributors/Devgather/TIARY?color=green&labelColor=black&style=flat-square
[github-contributors-link]: https://github.com/Devgather/TIARY/graphs/contributors
[github-forks-badge]: https://img.shields.io/github/forks/Devgather/TIARY?color=blue&labelColor=black&style=flat-square
[github-forks-link]: https://github.com/Devgather/TIARY/forks
[github-release-badge]: https://img.shields.io/github/v/release/Devgather/TIARY?color=red&labelColor=black&style=flat-square
[github-release-link]: https://github.com/Devgather/TIARY/releases
[github-stars-badge]: https://img.shields.io/github/stars/Devgather/TIARY?color=yellow&labelColor=black&style=flat-square
[github-stars-link]: https://github.com/Devgather/TIARY/stargazers
[sonar-coverage-badge]: https://img.shields.io/sonar/coverage/TIARY?server=https://sonarcloud.io&logo=sonarcloud&logoColor=white&color=f3702a&labelColor=black&style=flat-square
[sonar-link]: https://sonarcloud.io/project/overview?id=TIARY
[sonar-quality-gate-badge]: https://img.shields.io/sonar/quality_gate/TIARY?server=https://sonarcloud.io&logo=sonarcloud&logoColor=white&color=f3702a&labelColor=black&style=flat-square