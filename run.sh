#!/usr/bin/env bash
# Compilation et lancement de TechVest (Linux / macOS / Git Bash)
set -e
cd "$(dirname "$0")"

command -v javac >/dev/null || { echo "Java JDK 17+ requis : https://adoptium.net/"; exit 1; }

# Driver SQLite JDBC (téléchargé automatiquement depuis Maven Central si absent)
if [ ! -f lib/sqlite-jdbc.jar ]; then
  mkdir -p lib
  echo "Téléchargement du driver SQLite JDBC..."
  curl -sSL -o lib/sqlite-jdbc.jar \
    https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.1.3/sqlite-jdbc-3.46.1.3.jar
fi

mkdir -p bin database
find src -name "*.java" > bin/sources.txt
javac -encoding UTF-8 -d bin -cp "lib/*" @bin/sources.txt
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp "bin:lib/*:resources" com.techvest.Main
