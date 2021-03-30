**Methode de déployement du logiciel**
```
Dans le dossier principal contenant le projet, il y a 2 sous-dossiers parmi lesquels, on a :
    - Code Source : ce dossier contient le code source du logiciel (les fonctions, le code source de l'affichage de la fenêtre).
    - Executable : il contient le fichier .exe et .jar prêt à l'exécution.
NB : Il faut verrifier que vous ayez installé au moins JDK version 8 votre machine. Pour celà :
    1- Allez dans Panneau de Configuration -> Système et Sécurité -> Système -> Paramètres système avancés -> Variables d'environnements.
    2- Vérifier si la variable "JAVA_HOME" contient bien le chemain vers jdk 8, sinon, définissez-la avec la valeur
        "C:\Program files\Java\nomDuRepertoireDuJDK8".
    3- Lancez l'application contenu dans le repertoire "Executable" (le fichier ImageEditingv1.2.6.exe ou ImageEditing.jar) pour vérifier si elle fonctionne. Normalement, elle doit fonctionner.
```
**Vous pouvez aussi aller dans le code source et double-cliquer le fichier createJar.bat. Un fichier .jar exécutable sera créé automatiquement**