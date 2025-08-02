run:
	./gradlew lwjgl3:run --stacktrace

clean:
	./gradlew clean

jar:
	./gradlew lwjgl3:dist

web:
	./gradlew html:dist

webRun:
	cd html/build/dist/ && python3 -m http.server
