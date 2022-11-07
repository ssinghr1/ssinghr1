import NgramLM

class ImprovedNgramLM(NgramLM.NgramLM):
    """An improved version of n-gram language model with OOV handling.
    This class inherits all bahaviors from previous defined `NgramLM`.
    """

    def __init__(self, n: int):
        super(ImprovedNgramLM, self).__init__(n=n)
           
        self.a: int = 0.2 #add-a smoothing constant

    @overrides
    def next_word_candidates(self, context: Tuple[str]) -> List[str]:
        """Generates a list of tokens based on the given context, which would be
        later used as candidates for the next word prediction.
        
        Parameters
        ----------
        context : Tuple[str]
            A tuple of words describing the context for the next word.
            
        Returns
        -------
        words : List[str]
            A list of candidate tokens for the next word.
        """
        candidates = self.contexts[context]
        candidates.add('<UNK>')
        return list(candidates)
        ...

    @overrides
    def word_prob(self, context: Tuple[str], word: str) -> float:
        """Returns the probability of a word given a context. The context is a
        string of words, with length n-1. 
        
        Parameters
        ----------
        context : Tuple[str]
            A tuple of words describing the context for the next word.
        word : str
            The next word that the probability is computed for.
            
        Returns
        -------
        prob : float
            The estimated probability of the next word given the context.
        """

        complete_ngram = context + (word,)
        if complete_ngram in self.ngrams:
          counts_word = float(self.ngrams[complete_ngram])
        else:
          counts_word = float(self.ngrams[context + ('<UNK>',)])

        counts_context = float(self.ngram_contexts[context])
        prob = (counts_word+self.a)/(counts_context+self.a*len(self.vocab))

        return prob
        ...