install : clean
	touch data/tables/meta.csv
	echo "Table Name,Column Name,Column Type,Key,Indexed,References" >> data/tables/meta.csv

clean :
	rm -f data/app/*
	rm -f data/tables/*
	rm -f *.db
	rm -f *.lg