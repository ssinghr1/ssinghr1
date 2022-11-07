# Stratego console implementation

Rules can be found [here](https://daroolz.com/play/stratego/) <br>
Skips some of the more nuanced rules to avoid overcomplication. <br>
Refer to Implementations for details. 

Sample load from file:
```
 p o o l o p i m o p
 l s b y g o j j c o
 s b s j c l b l p s
 o i b o i b f b i i
 - - X X - - X X - -
 - - X X - - X X - -
 M P L I O P O O O P
 S O C C F O S B B L
 J O J Y P L B S L O
 J I I I S B G B B I
 r
```

Translates to: 
```
 p o o l o p i m o p
 l s b y g o j j c o
 s b s j c l b l p s
 o i b o i b f b i i
 - - X X - - X X - -
 - - X X - - X X - -
 M P L I O P O O O P
 S O C C F O S B B L
 J O J Y P L B S L O
 J I I I S B G B B I
 Current player turn
```
*NOTE*: Uppercase and Lowercase represent different payers. 