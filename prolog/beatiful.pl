beautiful([X,Y,Z]).
notSoBeautiful([A,B,C]).
theBeautifuls([X,Y,Z],[]).
queryBeautiful:-write('enter names of 3 beautiful girls'),read(X),read(Y),read(Z),X=read(X),Y=read(Y),Z=read(Z),beautiful([X,Y,Z]).

