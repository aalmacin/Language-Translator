# Google Translate Experiment

To start running, run the command below. Make sure to replace the path

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/path-to-credentials-file

./gradlew exec
```

## Deployment Manager
Audios bucket was created by running

```
gcloud deployment-manager deployments create --config=./storage.yml language-translator
```

## Example Commands

### Normal
```shell
./gradlew exec --args="Health.tsv Health-result.tsv en"
```

### Cloze
```shell
./gradlew exec --args="a.tsv a-out.tsv en cloze"
```

## Moving to Anki
```shell
cp audio/* ~/Library/Application\ Support/Anki2/[Account]/collection.media
```