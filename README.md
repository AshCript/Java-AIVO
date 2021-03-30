# ANALYSE D'IMAGE ET VISION PAR ORDINATEUR. (PHASE DE TEST)
# **Traitement d'image.**

    Projet de groupe.
    Membres :
        - RAKOTONDRABE As Manjaka Josvah.
        - RIJANIAINA Elie Fidèle.

**LES FONCTIONNALITES DU LOGICIEL**

    Traitement basique
    |__ Symetrie par rapport à X
    |__ Symetrie par rapport à Y
    |__ Symetrie par rapport à l'origine O
    Transformation ponctuelle
    |__ Niveau de gris
    |__ Inversion de niveau de gris
    |__ Inversion de couleur
    |__ Binarisation
    |   |__ Binarisation par seuillage
    |   |__ Binarisation par la methode d'Otsu
    |   |__ Contour
    |   |   |__ Contour par l'erosion [Avec la formule : contour = imageInitiale - erosion(imageInitiale)]
    |   |   |__ Contour personnel [On a trouvé un autre moyen de déterminer le contour avec une dizaine de lignes logique de code]
    |   |   |__ Contour en utilisant l'algorithme de la tortue de Papert
    |   |__ Etiquetage
    |   |__ Opérateurs fondamentaux :
    |   |   |__ Erosion
    |   |   |__ Dilatation
    |   |   |__ Ouverture [dilatation(erosion(imageInitiale)) : erosion suivi de dilatation]
    |   |   |__ Fermeture [erosion(dilatation(imageInitiale)) : dilatation suivi de l'erosion]
    |   |__ Opérateurs Complémentaires :
    |   |   |__ Tout ou rien 
    |   |   |__ Epaississement [imageInitiale - toutOuRien(imageInitiale)]
    |   |   |__ Amincissement [Algorithme personnel]
    |   |   |__ Top-hat à l'ouverture [th_Ouverture = imageInitiale - ouverture(imageInitiale)]
    |   |   |__ Top-hat à la fermeture [th_Fermeture = imageInitiale - fermeture(imageInitiale)]
    |   |__ Etalement de la dynamique
    |   |__ Spécifiaction d'histogramme
    |       |__ Egalisation
    |       |__ Inversion [Un test personnel]
    |
    |__ Transformation locale
        |__ Filtrage linéaire
        |   |__ Convolution discrète
        |   |   |__ Filtre de l'exemple
        |   |   |__ Filtre de Sobel 1980
        |   |   |__ Filtre personnel 1 [Algorithme personnel]
        |   |   |__ Filtre personnel 2 [Algorithme personnel]
        |   |__ Filtre moyen
        |   |__ Filtre Gaussien
        |__ Filtrage non linéaire
            |__ Lissage conservateur
            |__ Filtre median

La version de ce logiciel est encore assez basique du côté design. Elle permet juste de faire les traitements
d'image et repond à l'attente du sujet donné.
###
**Methode de déployement du logiciel**
    
    Dans le dossier principal contenant le projet, il y a 2 sous-dossiers parmi lesquels, on a :
        - Code Source : ce dossier contient le code source du logiciel (les fonctions, le code source de l'affichage de la fenêtre).
        - Executable : il contient le fichier .exe et .jar prêt à l'exécution.
    NB : Il faut verrifier que vous ayez installé au moins JDK version 8 votre machine. Pour celà :
        1- Allez dans Panneau de Configuration -> Système et Sécurité -> Système -> Paramètres système avancés -> Variables d'environnements.
        2- Vérifier si la variable "JAVA_HOME" contient bien le chemain vers jdk 8, sinon, définissez-la avec la valeur
            "C:\Program files\Java\nomDuRepertoireDuJDK8".
        3- Lancez l'application contenu dans le repertoire "Executable" (le fichier ImageEditingv1.2.3.exe ou ImageEditing.jar) pour vérifier si elle fonctionne. Normalement, elle doit fonctionner.
        ***Si il y a une erreur lors du lancement de ces fichiers exécutables, alors, allez dans la code source et double-cliquez le fichier createJar.bat      
**Vous pouvez aussi aller dans le code source et double-cliquer le fichier createJar.bat. Un fichier .jar exécutable sera créé automatiquement**
###
**LES ACTIONS POSSIBLES QUE L'UTILISATEUR PEUT EFFECTUER**
    
    1. Les raccourcis clavier :
        - Ctrl + F : Ouvrir un fichier image.
        - Ctrl + S : Sauvegarder l'image après traitement.
        - Ctrl + Z : Précédent (retour à l'image précédent avant le traitement courant).
        - Ctrl + Shift + Z : Suivant (retour à l'image suivant, après le traitement courant).
        - Ctrl + E : Refaire la même action de traitement.
        - Ctrl + H : Afficher l'histogramme de l'image.
        - Ctrl + Q : Quitter
**LE CODE SOURCE**
#
Dans le dossier Code source, il y a plusieurs fichiers(.java) . Les fichiers qui nous intéressent sont le fichier qui contient l'affichage, et celui qui contient les fonctions.
- Ce qui concerne l'affichage se trouve dans le fichier **PicFrame.java**. 

- Le fichier qui contient tous les fonctions (**Fonctions.java**) se trouve dans le dossier **com/as/func**. L'algorithme est bien documenté, puisqu'à chaque étape, il y a des commentaires qui expliquent la logique de l'algorithme.