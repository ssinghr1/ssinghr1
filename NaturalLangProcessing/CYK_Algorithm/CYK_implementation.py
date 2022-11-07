from nltk.metrics.aline import R
class Node:
    """ Equivalent to a non-terminal. Since our grammar is CNF, a node can have at
    most 2 children. Following 2 cases are possible:
    
    Case 1 -> child1 is a terminal symbol
    Case 2 -> both child1 and child2 are Nodes.
    """

    def __init__(self, symbol, child1, child2=None):
        self.symbol = symbol
        self.child1 = child1
        self.child2 = child2

    def __repr__(self):
        """Returns the string representation of a Node object."""
        return self.symbol
    
    def generate_tree(self) -> str:
        """Generates the string representation of the tree rooted at the current node.
        It is done via pre-order tree traversal.
        
        Returns
        -------
        str_tree : str
            The tree in its string form.
        """
        if self.child2 is None:
            return f"[{self.symbol} '{self.child1}']"
        return f"[{self.symbol} {self.child1.generate_tree()} {self.child2.generate_tree()}]"
    
class CYKParser(object):
    """A CYK parser which is able to parse any grammar in CNF. The parser object
    is created from a CNF grammar and can be used to parse any sentence.
    """
    def print_test():
          output = ''
          for i in range(len(parse_table)):
            output += '{'
            addition =''
            for j in range(len(parse_table[i])):
              addition += str([parse_table[i][j][n] for n in range(len(parse_table[i][j]))])
              addition += ','
            addition += '}\n'
            output += addition
          print(output)

    def __init__(self, grammar: str):
        """Creates a new parser object.

        Parameters
        ----------
        grammar : str
            Input grammar as a string of rules.
        """
        self.grammar: nltk.grammar.CFG = nltk.grammar.CFG.fromstring(grammar)
            
    def print_tree(self, parse_table: List[List[List[Node]]]):
        """Prints the parse tree starting with the start symbol.
        """
        start_symbol = self.grammar.start().symbol()
        final_nodes = [n for n in parse_table[-1][0] if n.symbol == start_symbol]
        if final_nodes:
            print("\nPossible parse(s):")
            trees = [node.generate_tree() for node in final_nodes]
            for tree in trees:
                print(tree)
        else:
            print("The given sentence is not contained in the language produced by the given grammar!")

    def parse(self, sentence: List[str]) -> List[List[List[Node]]]:
        """Does the actual parsing according to the CYK algorithm.
        
        Parameters
        ----------
        sentence : List[str]
            An input sentence in the form of list of tokens.
        
        Returns
        -------
        parse_table : List[List[List[Node]]]
            The resulting parse table for the sentence under the grammar.
        """
        num_tokens = len(sentence) 

        # parse_table[y][x] is the list of nodes in the x+1 cell
        # of y+1 row in the table. That cell covers the word below it
        # and y more words after.
        parse_table: List[List[List[Node]]] = [[[] for x in range(num_tokens - y)] for y in range(num_tokens)] 

        for s in range(1, num_tokens+1):
          for rule in self.grammar.productions():

            if rule.is_lexical() and sentence[s-1] == rule.rhs()[0]:
              
              Rv = rule.lhs()
              As = rule.rhs()[0]
              word_node = Node(As, None, None)
              parse_table[0][s-1].append(Node(Rv.symbol(), word_node))
               
        for l in range(2, num_tokens + 1):
          for s in range(0, num_tokens-l+1):
            for p in range(1, l):

              for rule in self.grammar.productions():

                left_locations = parse_table[p-1][s]
                right_locations = parse_table[l-p-1][s+p]

                if left_locations and right_locations:
                  if rule.is_nonlexical():

                    Ra = rule.lhs()
                    Rb = rule.rhs()[0]
                    Rc = rule.rhs()[1]

                    left_nodes = [node for node in left_locations if (node and node.symbol == Rb.symbol())]
                    right_nodes = [node for node in right_locations if (node and node.symbol == Rc.symbol())]

                    if left_nodes and right_nodes:
                        parse_table[l-1][s].extend([Node(Ra.symbol(), l_node, r_node) for l_node in left_nodes for r_node in right_nodes])

        return parse_table

def check_cnf() -> bool:
    nltk_grammar = nltk.grammar.CFG.fromstring(cnf_cfg_rules)
    return nltk_grammar.is_chomsky_normal_form()

    cnf_cfg_rule_check = check_cnf()

def main():
    cnf_cfg_rules = """
    S -> NP VP
    PP -> P NP
    NP -> Det N
    NP -> NP2 PP 
    NP2 -> Det N
    NP -> 'I'
    VP -> V NP
    VP -> VP PP
    Det -> 'an'
    Det -> 'my'
    N -> 'elephant'
    N -> 'pajamas'
    V -> 'shot'
    P -> 'in'
    """

    cnf_cfg_rule_check = check_cnf()
    print("This rule set is in Chomsky Normal Form? ", cnf_cfg_rule_check)

    parser = CYKParser(cnf_cfg_rules)
    parse_table = parser.parse("I shot an elephant in my pajamas".split())
    print(cnf_cfg_rules)
    print("I shot an elephant in my pajamas")
    parser.print_tree(parse_table)
    

