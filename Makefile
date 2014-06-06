all : clean
	mkdir data
	mkdir data/log
	touch data/meta.csv
	echo "Table Name,Column Name,Column Type,Key,Indexed,References" >> data/meta.csv

clean :
	rm -R -f data 2> /dev/null
	rm -f *.db 2> /dev/null
	rm -f *.lg 2> /dev/null
