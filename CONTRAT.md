# Équipe : 6
# Jeu : Gradius

# Contrat concernant le moteur de jeu

## Partie Modèle

### Géométrie
*  [x] __monde torique__
  + Explications: [Torus en X et en Y]
  + Démo : [Entité qui passe de haut en bas, et de gauche à droite] 

*  [x] __défilement / scrolling__
  + Explications: [Le vaisseau du joueur a une vitesse constante vers la droite, et le viewport a la même vitesse constante. De plus si le vaisseau bouge dans le viewport (grâce aux contrôles du joueur), le viewport lui conserve la même vitesse tout le temps.]
  + Démo : [Dézoom sur la map pour afficher au-delà du viewport + affichage des vecteurs vitesse des entités/obstacles] 
 
*  [ ] __taille de la map modifiable ?__
  + Explications: [...]
  + Démo : [...]

*  [x] __bords du monde__ 
  + Explications: [Le joueur est bloqué en haut et en bas dans le viewport via des obstacles visibles (des dunes de sable ou des murs en roche), et sur les cotés via des obstacles invisibles]
  + Démo : [Dézoom au-delà du viewport pour montrer que le vaisseau est bloqué]


### Génération de la map

*  [ ] __fixe__ :
  + Explications: [? sous quelle forme est-elle décrite ?] 
  + Démo : [...]

*  [x] __par configuration__ :
  + Explications: [Ensemble d'obstacles (entités) créés au préalable, de même dimension, choisis aléatoirement et placés au fur et à mesure avant le passage du viewport et supprimés après le passage du viewport.] 
  + Démo : [Dézoom pour voir l'apparition/disparition des obstacles quand le viewport se déplace.]

*  [x] __aléatoire__
  + Explications: [? déclenchée par ?]
  + Démo : [...]

*  [ ] __aléatoire avec mémorisation__
  + Explications: [? en cas de retour en arrière ?]
  + Démo : [...]


### Actions

*  [ ] __annulation d'une action__
  + Explications: [...]
  + Démo: [...]

*  [ ] __résolution de conflit lorsque deux entités veulent atteindre la même case__
  + Explications: [...]
  + Démo: [...]

### Affichage et animation 

*  [x]  __barre de santé / points de vie de chaque personnage__
  + Explications: [Nombre de cœur pour le vaisseau du joueur, qui diminue à chaque collision avec des projectiles ou des obstacles. Une fois touché, le vaisseau se met à clignoter et est invincible sur un temps définit, une fois ce temps écoulé,le vaisseau revient dans un état normal. Une fois tous les cœurs consommés, le vaisseau explose et la partie est finie.]
  + Démo : [Démo de la période d'invincibilité + cœurs affichés sur l'écran]

*  [ ] __animation lors de la création / apparition__
  + Explications: []
  + Démo : [...]

*  [x] __animation lors de la destruction / disparition__
  + Explications: [Vaisseau détruit -> Animation de destruction, animation d'explosion des ennemies]
  + Démo : [Destruction du vaisseau]

*  [ ] __animation en fonction de l'état du Bot (Waiting, Resting, ...)__
  + Explications: []
  + Démo : [...]

*  [x] __animations / affichage spécifiques__
  + Explications: [Particules de sable pour le dragon, animation des armes, animation des tirs, des explosions.]
  + Démo : [Démonstration du spawn du dragon et du tir avec les armes]


#### Adaptabilité = changement pendant le jeu
*  [x] changement de Stunt 
  + Explications: [Changement d'arme]
  + Démo: changement de façon de faire une action : [Changement d'arme et de type de tir] 

*  [ ] changement d'avatar 
  + Explications: [...]
  + Démo : changement d'image : [...] 

*  [x] changement de bot/FSM 
  + Explications: [À faible vie, le dragon cesse d'errer dans le tore pour attaquer le joueur (mode enragé)]
  + Démo : changement de comportement : [Affichage du nom du bot et passage en mode enragé]

      
## Partie Vue

  *  [ ] view port centré sur le joueur
    *  [ ] zoom
    *  [x] scrolling
  

## Mode debug

  *  [x] affichage du temps entre deux _tick_ (min, max, moyenne)
  *  [x] affichage du temps entre deux _paint_ (min, max, moyenne)
  *  [x] affichage du nombre de _FPS_ (min, max, moyenne)
  *  [x] gestion de la charge 
    + Démo: maximum d'entités actives : [...]  
      
  *  [x] affichage de l'action au dessus du personnage
  *  [x] affichage des bounding box    

## Partie Controlleur

#### Bot
  *  [ ] Bot en Java
  *  [ ] Bot en FSM Java
  *  [x] Bot en GAL + parser
  *  [x] Démo : modification du comportement des entités 
      *  [ ] en changeant de classe Bot 
      *  [ ] en changeant de FSM Java
      *  [x] en changeant le fichier `.gal` 

## Partie Stunt : Action unique _versus_ actions multiples simultanées 

  *  [ ] une action à la fois  
  *  [x] actions multiples (ex: sauter+tourner, avancer+tourner) 
  *  [ ] gestion des actions compatibles/incompatibles

# Contrat spécifique au jeu choisi

- Implémentation d'un dragon composé de plusieurs parties, qui se déplace sur la map, et qui s'enrage lorsqu'il perd + de la moitié de ses PV, et change de comportement pour viser le joueur (+ boost de vitesse).
  Le dragon est composé d'une tête, de segments de corps, et d'une queue. La tête est l'entité dont le bot prend les décisions de mouvement pour tout le corps et chaque segment suit le segment devant lui. Pour cela, on conservera un historique des positions/orientations de la tête pour les transmettre aux parties du corps et un léger délai sera appliqué pour les rotations et les déplacements pour permettre des mouvements fluide.
  Le dragon sera la seule entité pouvant traverser le tore en Y.
  On aura un automate pour la tête avec les états suivants :
	- Mouvement
	- Mourir
	- Prendre des dégats
  et un automate pour les parties du corps :
	-  Mouvement
	- Mourir
- Implémentation de 3 armes pour le vaisseau du joueur : 
	- balles "classiques" : vont tout droit par rafales de 3
	- Laser : tirs circulaires qui s'agrandissent progressivement
- Ennemis :
	- vaisseaux classiques, qui arrivent par vagues, avec des comportements différents. (ex: mouvement différents). Les types de vagues sont créés au préalable, puis choisies aléatoirement. 
	- Factory de vaisseaux : posé au sol et créé des vaisseaux ennemis
	- Boss final : Grosse entité composite (composée de plusieurs entités) avec beaucoup de PV, dont certaines parties génèrent des vaisseaux ou effectuent des tirs de zones.
- BONUS :
	- Boost de vitesse : peut être drop par un ennemi à sa mort. Boost temporaire
	- Power up triple shot : temporaire
	- Power up shield : temporaire, tank un dégat, disparaît si dégat subis
	- Système d'esquive permettant d'éviter les collisions avec les projectiles pendant un court instant (+ léger déplacement). Cooldown de 3-5 secondes.
	- Système de points gagnés en tuant des ennemis
	- Nouvelle arme : missiles : vont tout droit, un par un, et font plus de dégats que les balles classiques.