writelist([]).
writelist([H|T]):-write(H),writelist(T).

