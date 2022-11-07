# CYK Parsing Algorithm Implementation

Implementation of CYK algorithm for determining whether a set of grammar rules is in Chomsky Normal Form or not. 

Pseudocode that this implementation is based on (found on [wikipedia](https://en.wikipedia.org/wiki/CYK_algorithm)):
```python
let the input be a string I consisting of n characters: a1 ... an.
let the grammar contain r nonterminal symbols R1 ... Rr, with start symbol R1.
let P[n,n,r] be an array of booleans. Initialize all elements of P to false.
let back[n,n,r] be an array of lists of backpointing triples. Initialize all elements of back to the empty list.

for each s = 1 to n
    for each unit production Rv → as
        set P[1,s,v] = true

for each l = 2 to n -- Length of span
    for each s = 1 to n-l+1 -- Start of span
        for each p = 1 to l-1 -- Partition of span
            for each production Ra    → Rb Rc
                if P[p,s,b] and P[l-p,s+p,c] then
                    set P[l,s,a] = true, 
                    append <p,b,c> to back[l,s,a]

if P[n,1,1] is true then
    I is member of language
    return back -- by retracing the steps through back, one can easily construct all possible parse trees of the string.
else
    return "not a member of language"
```

Example visualization of algorithm on sample sentence and grammar used in this implementation:
<table>
<tr>
<th> Rules </th>
<th> Parse Table </th>
</tr>
<tr>
<td>

1. S -> NP VP
2. PP -> P NP
3. NP -> Det N
4. NP -> NP2 PP 
5. NP2 -> Det N
6. NP -> 'I'
7. VP -> V NP
8. VP -> VP PP
9. Det -> 'an'
10. Det -> 'my'
11. N -> 'elephant'
12. N -> 'pajamas'
13. V -> 'shot'
14. P -> 'in'

</td>
<td>


| **I** 	| **Shot** 	| **An** 	| **Elephant** 	| **In** 	| **My** 	| **Pajamas** 	|
|-----------|-----------|-----------|---------------|-----------|-----------|---------------|
| NP    	| V        	| Det    	| N            	| P      	| Det    	| N           	|
|       	|          	| NP     	|              	|        	| NP     	|
|       	| VP       	|        	|              	| PP     	|        	|             	|
| S     	|          	|        	|              	|        	|        	|             	|
| <br>   	|          	|        	|              	|        	|        	|             	|
|       	| VP       	|        	|              	|        	|        	|             	|
| S     	|          	|        	|              	|        	|        	|             	|


</td>
</tr>
</table>