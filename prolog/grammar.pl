
sentence-->noun,verb_phrase.
verb_phrase-->verb,noun.
noun-->[bob].
noun-->[david].
noun-->[osebe].
verb-->[likes].
verb-->[hates].
test:-write('write a 3 word sentence '),nl,read(X),read(Y),read(Z),sentence([X,Y,Z],[]).
