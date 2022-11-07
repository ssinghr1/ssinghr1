import random
import math
import re
import os
import urllib
import json
from typing import *
from collections import Counter, defaultdict

import numpy as np
import nltk
import matplotlib.pyplot as plt
from overrides import overrides
from nltk.tokenize import RegexpTokenizer, sent_tokenize

import NgramLM
import Perplexity
import NgramLMWithLaplaceSmoothing

nltk.download('punkt')

def download_data():
    def _download(url: str, filename: str) -> str:
        txt = urllib.request.urlopen(url)
        with open(filename, 'w') as f:
            f.write(txt.read().decode('utf-8'))
        
    _download('https://cs.stanford.edu/people/karpathy/char-rnn/warpeace_input.txt', 'warpeace_input.txt')
    _download('http://www.gutenberg.org/files/1399/1399-0.txt', '1399-0.txt')

def generate_ngrams(text: List[str], n: int) -> Counter:
    """Generates all n-grams (i.e. n-1 context words) for the given text.

    Parameters
    ----------
    text : List[str]
        Input text (list of strings) after tokenization.
    n : int
        n-gram parameter (must be greater than or equal to 1).

    Returns
    -------
    ngrams : Counter
        Output n-grams dictionary as {ngram: count} (Dict[Tuple, int]), 
        where `ngram` is a n-gram tuple and `count` is an integer count.
        e.g. ('Mary','has') and value as count of the n-gram in the text.
    """
    assert (isinstance(n, int) and n > 0)

    ngrams = Counter()
    text_copy=text.copy()

    for i in range(n-1):
          text_copy.insert(0, '~')
          text_copy.insert(len(text_copy), '~')
          
    for i in range(len(text_copy)-n+1):
      grams = tuple(text_copy[i:i+n])
      ngrams[grams] +=1

    return ngrams


def generate_ngrams_sentences(text: List[List[str]], n: int) -> Counter:
    """Generates n-grams for each sentence and aggregates them."""
    all_ngrams = Counter()

    for sentence in text:
        all_ngrams.update(generate_ngrams(sentence, n))
        
    return all_ngrams

def compute_empirical_distribution(model: NgramLM, context: Tuple[str], num_samples: int) -> Dict[str, float]:
    """Computes an empirical distribution for the next word conditioning on the given context.
    
    Parameters
    ----------
    model : NgramLM
        A trained Ngram Language Model.
    context : Tuple[str]
        The context used to predict a next word.
    num_samples : int
        The number of samples to be drawn to compute the empirical distribution.
        
    Returns
    -------
    emp_distr : Dict[str, float]
        An empirical distribution for the next word 
    
    """
    emp_distr: Dict[str, float] = {}
    
    for i in range(0, num_samples):
      word = model.random_word(context)
      emp_distr[word] = (emp_distr[word] + 1.0) if word in emp_distr else 1.0
    for word in emp_distr:
      emp_distr[word] = emp_distr[word]/num_samples
    ...
    
    return emp_distr


def main():

    download_data()

    try:
        with open('warpeace_input.txt', 'r') as file:
            corpus_raw = file.read().replace('\n', ' ')
    except FileNotFoundError:
        with open('../../warpeace_input.txt', 'r') as file:
            corpus_raw = file.read().replace('\n', ' ')

    sentences = sent_tokenize(corpus_raw)

    corpus = []
    tokenizer = RegexpTokenizer(r'\w+')
    for sentence in sentences:
        tokens = tokenizer.tokenize(sentence)
        corpus.append([token.lower() for token in tokens])

    print("Corpus has {} sentences".format(len(corpus)))

    # Implementation test for a few sentences from corpus
    text = corpus[:10]
    print(text)

    ngrams = generate_ngrams_sentences(text, 3)
    print(ngrams)

    trigramlm = NgramLM.NgramLM(3)
    for sentence in corpus:
        trigramlm.update(sentence)

    num_tokens = 0
    for sentence in corpus:
        for word in sentence:
        num_tokens += 1
    print("size of training corpus ", num_tokens)

    trigramlm_vocab_size = len(trigramlm.vocab)
    print("size of vocab ", trigramlm_vocab_size)

    sys_empy_distr = compute_empirical_distribution(trigramlm, ('we', 'have'), 10000)

    generated_text = str(trigramlm.random_text(100))
    print("sample trigram text ", generated_text)

    qgramlm = NgramLM.NgramLM(4)
    for sentence in corpus:
        qgramlm.update(sentence)
    
    qgram_generated_text = str(qgramlm.random_text(100))
    print("sample qgram output ", qgram_generated_text)

    trigram_perp_on_training = Perplexity.perplexity(trigramlm, corpus)
    print("trigram perplexity ", trigram_perp_on_training)

    qgram_perp_on_training = Perplexity.perplexity(qgramlm, corpus)
    print("qgram perplexity ", qgram_perp_on_training)

    #going to test against a held-out from Leo Tolstoy's Anna Karenina
    try:
        with open('1399-0.txt', 'r') as file:
            dev_raw = file.read().replace('\n', ' ')
    except FileNotFoundError:
        with open('../../1399-0.txt', 'r') as file:
            dev_raw = file.read().replace('\n', ' ')
    
    pattern = "Chapter 1(.*)Chapter 2"
    dev_ch1 = re.search(pattern, dev_raw).group(1)

    sentences = sent_tokenize(dev_ch1)

    dev_text = []
    tokenizer = RegexpTokenizer(r'\w+')
    for sentence in sentences:
        tokens = tokenizer.tokenize(sentence)
        dev_text.append([token.lower() for token in tokens])

    print("Dev data has {} sentences".format(len(dev_text)))

    trigram_perp_on_dev = Perplexity.perplexity(trigramlm, dev_text)
    print("the perplexity is ", trigram_perp_on_dev)


    trigramlm_laplace = NgramLMWithLaplaceSmoothing.NgramLMWithLaplaceSmoothing(3)
    for sentence in corpus:
        trigramlm_laplace.update(sentence)
    trigram_laplace_perp_on_training = Perplexity.perplexity(trigramlm_laplace, dev_text)
    print("Laplace perplxeity is: ", trigram_laplace_perp_on_training = perplexity(trigramlm_laplace, dev_text))

    improved_trigramlm = ImprovedNgramLM(3)
    for sentence in corpus:
        improved_trigramlm.update(sentence)
    print("Improved Trigram perplexity ", Perplexity.perplexity(improved_trigramlm, dev_text))
    