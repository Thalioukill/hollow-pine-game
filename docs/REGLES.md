# Règles de Hollow Pine

Ce document détaille les règles et les mécaniques du jeu. Pour l'installer et le lancer, voir le [README](../README.md).

## Principe

Vous affrontez **Leshy**, un adversaire contrôlé par l'application, dans un duel de cartes au tour par tour. Chacun pose des cartes symbolisant des animaux sur un plateau ; ils attaquent à la fin du tour et font bouger une **balance** qui mesure l'écart de score. Le premier qui obtient **5 points d'écart** en sa faveur remporte la partie, et il faut gagner **3 parties** à la suite pour gagner le jeu.

## Le plateau

- Deux lignes de **quatre emplacements** : vous posez vos cartes sur la ligne du bas, Leshy sur celle du haut.
- Une ligne supplémentaire au-dessus du plateau **annonce les cartes que Leshy jouera au tour suivant**, ce qui vous permet d'anticiper.
- À droite de votre main se trouve votre **pioche**, avec le nombre de cartes restantes.
- La **balance**, à gauche du plateau, indique l'écart de score.
- Des **obstacles** peuvent occuper certains emplacements au début de la partie.

## Les animaux

Chaque animal possède des points d'**attaque**, des points de **vie**, un **coût en sang**, un **coût en os**, et éventuellement un ou plusieurs **pouvoirs**. Une même carte peut exister en plusieurs exemplaires dans la pioche, dans la main et sur le plateau.

| Nom | Attaque | Vie | Sang | Os | Volant | Pouvoir |
|---|---|---|---|---|---|---|
| Chat | 0 | 1 | 1 | 0 | non | Nombreuses vies |
| Grizzly | 4 | 6 | 3 | 0 | non | |
| Coyote | 2 | 1 | 0 | 4 | non | |
| Moineau | 1 | 2 | 1 | 0 | oui | |
| Corbeau | 2 | 3 | 2 | 0 | oui | |
| Écureuil | 0 | 1 | 0 | 0 | non | |
| Hermine | 1 | 3 | 1 | 0 | non | |
| Louveteau | 1 | 1 | 1 | 0 | non | Croissance |
| Loup | 3 | 2 | 2 | 0 | non | |
| Punaise | 1 | 2 | 0 | 2 | non | Puant |
| Élan | 2 | 4 | 2 | 0 | non | Coureur |
| Vipère | 1 | 1 | 2 | 0 | non | Contact mortel |
| Porc-épic | 1 | 2 | 1 | 0 | non | Piques pointues |

### Les coûts : sang et os

Pour poser un animal, il faut payer son coût :

- **Gouttes de sang** : nombre d'animaux à vous que vous devez **sacrifier** sur le plateau.
- **Os** : nombre d'os à dépenser. Vous gagnez des os quand vos animaux meurent (tués ou sacrifiés).

Sacrifier un animal libère son emplacement et vous rapporte un os. Les obstacles ne peuvent pas être sacrifiés.

## Les pouvoirs

| Pouvoir | Effet |
|---|---|
| **Nombreuses vies** | La carte reste sur le plateau quand elle est sacrifiée. |
| **Croissance** | L'animal se transforme en loup au tour suivant, sur le même emplacement. |
| **Puant** | Réduit de 1 l'attaque de la carte qui lui fait face. |
| **Coureur** | Après son attaque, l'animal se déplace d'un emplacement vers la droite ; si c'est bloqué, vers la gauche ; si les deux sont bloqués, il reste en place. |
| **Contact mortel** | Si l'animal blesse une autre créature (pas un obstacle), celle-ci meurt. |
| **Piques pointues** | Quand l'animal est attaqué, il inflige 1 dégât à l'attaquant. |

Le nom du pouvoir est affiché sur la carte, et à côté de son nom dans votre main.

## Les obstacles

| Nom | Vie |
|---|---|
| Rocher | 5 |
| Sapin | 3 |

Un obstacle n'a que des points de vie. Il protège l'emplacement qu'il occupe et empêche d'y poser un animal, mais il peut être détruit en l'attaquant comme n'importe quelle carte. Selon la partie, un obstacle est placé d'un côté ou de l'autre du plateau (par exemple un Sapin chez Leshy en première partie, un Rocher chez vous en troisième).

## Déroulement d'un tour

1. **Pioche** : vous pouvez piocher une carte (une seule par tour).
2. **Pose** : vous placez autant de cartes que vous voulez, tant que vous avez des emplacements libres et que vous payez leur coût.
3. **Attaques** : à la fin de votre tour, chacun de vos animaux attaque.
   - Si une carte adverse lui fait face, elle perd autant de points de vie que l'attaque de l'animal.
   - S'il n'y a personne en face, l'attaque touche directement l'adversaire et fait bouger la balance.
   - Les animaux **volants** attaquent directement l'adversaire, même si une carte leur fait face.
4. **Tour de Leshy** : il joue selon les mêmes règles, avec un comportement entièrement prévu à l'avance (pas d'aléatoire).

Un message en fin de tour indique les dégâts infligés.

## Les parties

- Le jeu est composé de **3 parties**, qu'il faut gagner à la suite. Chacune oppose à un Leshy différent.
- Vous commencez avec une pioche de **15 cartes** (7 écureuils, 2 hermines, et un louveteau, un élan, un loup, une vipère, une punaise et un porc-épic), mélangée au début de chaque partie. Vous prenez **4 cartes en main** au départ.

### À la fin de la deuxième partie

1. **Nouvelle carte** : le jeu vous propose deux cartes que vous ne possédez pas encore, et vous en choisissez une pour votre pioche.
2. **Pierre de sacrifice** : vous choisissez une carte de votre pioche qui possède un pouvoir et vous la sacrifiez (elle disparaît définitivement) pour récupérer son pouvoir et l'ajouter à une autre carte.

Si la carte choisie possède déjà ce pouvoir, le jeu vous prévient : vous pouvez en choisir une autre, ou insister, auquel cas la carte est sacrifiée sans que le pouvoir soit ajouté.

## Pour aller plus loin

Le code est organisé en packages (`game`, `model`, `view`...) : voir la section « Structure du dépôt » du [README](../README.md). Le diagramme de classes se trouve dans le dossier `uml/`.
