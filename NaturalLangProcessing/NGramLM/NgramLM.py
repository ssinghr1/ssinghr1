class NgramLM(object):
    """A basic n-gram language model without any smoothing."""

    def __init__(self, n: int):
        assert (isinstance(n, int) and n > 0)
        
        self.n: int = n
        self.vocab: Set[str] = set()  # A set of all words appearing in the corpus.
        self.ngrams = Counter()  # count(ABC) - Dict[Tuple, int]
        self.ngram_contexts = Counter()  # count(AB) - Dict[Tuple, int]
        self.contexts = defaultdict(set)  # {AB: {C1,C2,C2}} - Dict[Tuple, Set[str]]
        
    def generate_ngrams(self, text: List[str]) -> Counter:
        """Generates all n-grams (i.e. n-1 context words) for the given text.
        Assumes n is defined at the class initialization

        Parameters
        ----------
        text : List[str]
            Input text (list of strings) after tokenization.

        Returns
        -------
        ngrams : Counter
            Output n-grams dictionary as {ngram: count} (Dict[Tuple, int]), 
            where `ngram` is a n-gram tuple and `count` is an integer count.
            e.g. ('Mary','has') and value as count of the n-gram in the text.
        """
        return generate_ngrams(text, self.n)
    
    def generate_ngrams_sentences(self, text: List[List[str]]) -> Counter:
        """Generates n-grams for each sentence and aggregates them."""
        return generate_ngrams_sentences(text, self.n)

    def update(self, text: List[str]):
        """Updates the model n-grams based on the given text input.
        
        Parameters
        ----------
        text : List[str]
        """
        text_copy = text.copy()

        new_ngrams = generate_ngrams(text_copy, self.n)
        self.ngrams.update(new_ngrams)     
        
        for ngram_tuple in new_ngrams:
          for word in ngram_tuple:
            self.vocab.add(word)

          context = tuple(ngram_tuple[0:self.n-1])
          if context in self.ngram_contexts:
            self.ngram_contexts[context] += new_ngrams[ngram_tuple]
          else:
            self.ngram_contexts[context] = self.ngrams[ngram_tuple]

          self.contexts[context].add(ngram_tuple[self.n-1])
        ...

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
        if word in self.vocab:
          counts_context = float(self.ngram_contexts[context])
          counts_word = float(self.ngrams[complete_ngram])
          if counts_context == 0 or counts_word == 0:
            return 0 
          prob = counts_word/counts_context
          return prob
        else:
          return 0
        ...
        
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
        return list(self.contexts[context])
        ...

    def random_word(self, context: Tuple[str]) -> str:
        """Generates a random word based on the given context.
        
        Parameters
        ----------
        context : Tuple[str]
            A tuple of words describing the context for the next word.
            
        Returns
        -------
        word : str
            One randomly drawn word from the distribution defined given the context.
        """
        assert context in self.contexts, f'Encountered unseen context={context}.'
        
        candidates = self.next_word_candidates(context)
        
        options=[]
        for candidate in candidates:
          full_ngram = context + (candidate,)
          for i in range(self.ngrams[full_ngram]):
            options.append(candidate)

        rand_ind = random.randint(0, len(options)-1)
        return options[rand_ind]
        ...

    def random_text(self, length: int) -> List[str]:
        """Generates random text of the specified word length excluding "~" (BOS).
        
        Parameters
        ----------
        length : int
            The designated length to generate.
        
        """
        output =[]
        for i in range(self.n-1):
          output.append('~')
        for i in range(1, length):
          context_thus_far = tuple(output[-(self.n-1):])
          output.append(self.random_word(context_thus_far))
        
        return output
        ...