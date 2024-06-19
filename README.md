
<!-- Plugin description -->
## ✨Description
TmaxGPT
IntelliJ-based plugin to refactor selected code using LLM models.
This intelliJ plugin uses the tuned mistralai/Mixtral-8x7B-Instruct-v0.1 model.

## ✨Usage
The following five features are available:
- Chatbot(alt+C)
- RefactorGPT(alt+R)
- JavaDoc(alt+J)
- GenerateCode(alt+G)
- CodeSummary(alt+S)

explanation:
- Chatbot: You can use chatgpt within the IDE using the openai API.
- RefactorGPT: LLM refactors the dragged code.
- JavaDoc: LLM creates a javadoc comment for the dragged code.
- GenerateCode: When you write a comment, LLM generates code based on it.
- CodeSummary: LLM provides a code summary and explanation for the dragged code.

click to Download >
[Download the plugin](https://github.com/fbwogur121/Internship_TmaxSoft/raw/feature/11/TmaxGPT-0.0.1.zip)

To apply it to your project, please follow the steps below:
- IntelliJ > Settings
- Plugins
- Tap the settings icon
- Install Plugin from Disk
- Select the downloaded .zip file
- restart IntelliJ

## ✨Structure
```
┌── src                     			      # Plugin 메인로직
│   ├── main             			            # plugin에 대한 코드 작성
│   │   ├── kotlin           		      	      # plugin 개발 언어
│   │ 	│   ├── com.Tmax                            # 플러그인 패키지 name: com.Tmax.tmaxPlugin
│   │ 	│ 	│   ├── tmaxPlugin                        # 주요 동작 코드 패키지
│   │ 	│ 	│ 	│   ├── action                          # IntelliJ IDEA의 커스텀 액션을 정의
│   │ 	│ 	│ 	│ 	│   ├── ChatGptAction                  # openai api를 사용하는 ChatBot
│   │ 	│ 	│ 	│ 	│   ├── CodeSummaryAction              # 제공된 코드에 대한 요약을 출력하는 model
│   │ 	│ 	│ 	│ 	│   ├── GenerateCodeAction             # 제공된 자연어 주석에 대한 코드를 생성, 출력하는 model
│   │ 	│ 	│ 	│ 	│   ├── JavaDocAction                  # 제공된 코드과 함께 JavaDoc 주석을 생성, 출하는 model
│   │ 	│ 	│ 	│ 	│   ├── RefactorGptAction              # 제공된 코드에 대한 refactored code를 출력하는 model
│   │ 	│ 	│ 	│   ├── api                              # OpenAI 와 api통신하기 위한 폴더 -> retrofit2로 통신
│   │ 	│ 	│ 	│ 	│   ├── ChatGptApi                     # chatgpt api interface
│   │ 	│ 	│ 	│ 	│   ├── ChatGptApiClient               # chatgpt client object
│   │ 	│ 	│ 	│   ├── dialog                           # LLM model UI 작업폴더
│   │ 	│ 	│ 	│ 	│   ├── ChatBotDialog                  # ChatBot UI
│   │ 	│ 	│ 	│ 	│ 	│   ├── ChatGptPanel                 # ChatBot UI 내부 동작기능
│   │ 	│ 	│ 	│ 	│ 	│   ├── ChatGptToolWindowFactory     # ChatGPT를 통합하는 도구 창을 생성하는 클래스
│   │ 	│ 	│ 	│ 	│   ├── CodeSummaryDialog              # CodeSummaryDialog UI
│   │ 	│ 	│ 	│ 	│   ├── GenerateCodeDialog             # GenerateCode model UI
│   │ 	│ 	│ 	│ 	│   ├── JavaDocDialog                  # Javadoc model UI
│   │ 	│ 	│ 	│ 	│   ├── RefactorGptDialog              # RefactorGpt model UI
│   │ 	│ 	│ 	│   ├── dto                              # chatGPT/Model과
│   │ 	│ 	│ 	│ 	│   ├── message                        # 각 model에 전달할 메세지 data class 객체화(플러그인 사용시 드래그한 부분)
│   │ 	│ 	│ 	│ 	│ 	│   ├── ChatGptMessage               # ChatGpt에 넘기는 메세지
│   │ 	│ 	│ 	│ 	│ 	│   ├── CodeSummaryMessage           # CodeSummary model에 넘기는 메세지
│   │ 	│ 	│ 	│ 	│ 	│   ├── GenerateCodeMessage          # GenerateCode model에 넘기는 메세지
│   │ 	│ 	│ 	│ 	│ 	│   ├── JavaDocMessage               # JavaDoc 생성 model에 넘기는 메세지
│   │ 	│ 	│ 	│ 	│ 	│   ├── RefactorGptMessage           # RefactorGpt model에 넘기는 메세지
│   │ 	│ 	│ 	│ 	│   ├── request                        # 실제 프롬프트가 들어가있는 폴
│   │ 	│ 	│ 	│ 	│ 	│   ├── ChatBotRequest               # chatGPT model 설정(현재는 3.5 turbo), request string
│   │ 	│ 	│ 	│ 	│ 	│   ├── CodeSummaryRequest           # CodeSummary model request string
│   │ 	│ 	│ 	│ 	│ 	│   ├── GenerateCodeRequest          # GemerateCode model request string
│   │ 	│ 	│ 	│ 	│ 	│   ├── JavaDocRequest               # JavaDoc 생성 model request string
│   │ 	│ 	│ 	│ 	│ 	│   ├── RefactorGptRequest           # RefactorGpt model request string
│   │ 	│ 	│ 	│ 	│   ├── response                       # chatGPT api 응답
│   │ 	│ 	│ 	│ 	│ 	│   ├── ChatGptResponse              # 응답 정제 data class
│   │ 	│ 	│ 	│ 	│   ├── Refactord                      # LLM model들의 응답을 담는 data class
│   │ 	│ 	│ 	│   ├── exception                        # chatGPT호출 실패시 예외처리
│   │ 	│ 	│ 	│ 	│   ├── ChatGptAuthenticationException # api key error exception
│   │ 	│ 	│ 	│ 	│   ├── ChatGptFetchFailureException   # chatGPT 자체 error exception
│   │ 	│ 	│ 	│   ├── service                          # Service단
│   │ 	│ 	│ 	│ 	│   ├── ChatBotSercive                 # chatGPT service
│   │ 	│ 	│ 	│ 	│   ├── CodeSummarySercive             # gpu server와 통신-model 선택, 파라미터값 설정
│   │ 	│ 	│ 	│ 	│   ├── GenerateCodeSercive            # gpu server와 통신-model 선택, 파라미터값 설정
│   │ 	│ 	│ 	│ 	│   ├── JavaDocSercive                 # gpu server와 통신-model 선택, 파라미터값 설정
│   │ 	│ 	│ 	│ 	│   ├── RefactorGptSercive             # gpu server와 통신-model 선택, 파라미터값 설정
│   │ 	│ 	│ 	│   ├── settings                         # chatGPT api 연동 관련 설정폴더
│   │ 	│ 	│ 	│ 	│   ├── SettingsConfig                 #
│   │ 	│ 	│ 	│ 	│   ├── SettingsForm                   #
│   │ 	│ 	│ 	│ 	│   ├── SettingsState                  # secret.kt의 api key 획득
│   │ 	│ 	│   ├── secret.kt           	         # openai api key를 보관하는 폴더 > .gitignore 필수
│   │ 	│   ├── kotlin.iml                         	 # 모듈의 소스 폴더와 JDK 상속 설정 지정
│   │   ├── resources            		             #
│   │ 	│   ├── messages                             #
│   │ 	│ 	│   ├── MyBundle.properties           	 #
│   │ 	│   ├── META-INF        	                 # 플러그인 대표설정
│   │ 	│ 	│   ├── plugin.xml           	         # plugin model 그룹, 단축키 설정
│   │ 	│ 	│   ├── plugin.svg           	         # 플러그인 대표이미지(현재 tmaxsoft 회사로고로 되어있음)
│   ├── test              			           # test 폴
├── .gitignore                     		     # git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳
├── build.gradle.kts                         # gradle 의존성, 버전관리 등 프로젝트 정보
├── CHANGELOG.md                             # TmaxGpt Release Note
├── gradle.properties                        # gradle project 속성관리
├── gradlew                                  # Gradle 빌드를 시작하기 위 스크립트
├── gradlew.bat                              # Gradle 빌드를 시작하기 위해 Windows에서 실행될 수 있는 배치 스크립트
├── README.md            	             # README 파일
└── settings.gradle.kts                      # 프로젝트 이름
```

## ✨Reference
- [Tmaxsoft 2024-1 Internship](https://team-keu0abwyhwvb.atlassian.net/wiki/spaces/SD/overview)
- [IntelliJ TmaxPlugin 인수인계서_류재혁](https://team-keu0abwyhwvb.atlassian.net/wiki/spaces/SD/pages/28835965/IntelliJ+plugin)
- [GitLab](https://team-keu0abwyhwvb.atlassian.net/wiki/spaces/SD/pages/28835965/IntelliJ+plugin)

## Version
version 0.0.1 : Released on June 21, 2024
<!-- Plugin description end -->
