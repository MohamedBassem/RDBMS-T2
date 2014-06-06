all : clean
	touch data/meta.csv
	echo "Table Name,Column Name,Column Type,Key,Indexed,References" >> data/meta.csv

clean :
	rm -R -f data/*
	rm -f *.db
	rm -f *.lg
	mkdir data/log
