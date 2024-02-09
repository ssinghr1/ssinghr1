

def improvedngramLm(self...):
    
    n2gram = ngram(2)
    n3gram = ngram(3)
    n4gram = ngram(4)
    
    def word_prob(self...):
        prob2 = 
        prob3=
        prob4=
        
        prop pool = backoff(prob1, prob2, prob3)
        #w1*p1 + w2*p2 +w3*p3 all divided by w1+w2+w3 or whatrever it says it is onlien
        
    

def optimize():
    
    improvedngramLm = improvedngramLM() # creates an improvewd model object
    
    weight1s = [i/100 for i in range(0.0,200.0)] #[0, .01, .02, 0.3...2]
    weight2s = [i/100 for i in range(0.0,200.0)]

    min_perp_until_now = 1000000000000
    opt = {0,0}
    for w1 in weight1s:
        for w2 in weight2s:
            curr_perp = Perplexity(improvedngramLM, w1, w2) #computes the perplexity of a improved model using given weights. 
            opt = {w1, w2} if curr_perp < min_perp_until_now

    print(w1, w2, min_perp_until_now)

    #assumes only n=2 and n=3 ngrams
    
    #main idea is gert w1 and w2
    
    