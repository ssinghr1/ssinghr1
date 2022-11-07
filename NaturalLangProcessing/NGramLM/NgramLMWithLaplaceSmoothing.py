import NgramLM

class NgramLMWithLaplaceSmoothing(NgramLM.NgramLM):
    """An n-gram language model with OOV handling and Laplace smoothing.
    This class inherits all bahaviors from previous defined `NgramLM`.
    """

    def __init__(self, n: int):
        super(NgramLMWithLaplaceSmoothing, self).__init__(n=n)
    
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
        candidates= self.contexts[context]
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
        counts_word = float(self.ngrams[complete_ngram])

        counts_context = float(self.ngram_contexts[context])
        prob = (counts_word+1)/(counts_context+len(self.vocab))

        return prob
        ...