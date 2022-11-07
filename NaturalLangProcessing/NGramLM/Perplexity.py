def perplexity(model: NgramLM, data: List[List[str]]) -> float:
    """Function to compute perplexity of ngram LM.

    Parameters
    ----------
    model : NgramLM
        A class object denoted a trained `NgramLM`.
    text : List[List[str]]
        A list of sentences, where each sentence is a list of tokens.

    Returns
    -------
    perp : float
        Perplexity of the LM on given string.
    """
    # TODO: Your code here
    data_copy = []

    for sentence in data:
      data_copy.append(sentence.copy())

    ngrams = model.generate_ngrams_sentences(data_copy)
    N = sum(ngrams.values())
   
    log_prob_sum = 0
    for ngram in ngrams:
      context = tuple(ngram[0:model.n-1])
      word_i = ngram[model.n-1]
      word_i_prob = model.word_prob(context, word_i)
      if word_i_prob == 0:
        return math.inf
      log_word_i_prob = math.log(word_i_prob)
      log_prob_sum += log_word_i_prob * ngrams[ngram]
    
    perplexity = (math.e**((-1/N) * log_prob_sum))
    return perplexity

    