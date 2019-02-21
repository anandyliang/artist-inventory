# artist-inventory
A really basic inventory system that an artist can use when selling at conventions.

This is based off a grocery store inventory system that I made in school but I adopted it and made it a lot more flexible. The main use case for this is selling at conventions, it'll help you keep track of sales as well as inventory instead of doing it on paper. I'm aware it's not very intuitive to use as it is right now, but I've been toying with the idea of using a SQLite database instead of just using textfiles but that'll probably be in another project since it's basically a rewrite. I also want to eventually do an android app or something but we'll see. I will definately make sure it's csv compatabile so its excel/sheet friendly.

## How do I use this?

This program is reliant on two text files inside a 'files' folder inside the same folder as the actual program
- Categories.txt
- Inventory.txt


### Categories.txt

This file provides info about the types of items you will be selling as well their unit price and any applicable "combo deals" That may be available.

|Line Number|Content|
|-----------|-------|
|Line 1     |Category Description|
|Line 2|Combo Quantity|
|Line 3|Combo Price|

E.g.

```
2" Double-sided
10
3
25
```
The above represents $10 per 2" Double-sided charm or $25 for 3.

### Inventory.txt

This is actually the file where the inventory is kept. My program will expect the following input.

|Line Number|Content|
|-----------|-------|
|Line 1     |Category Name|
|Line 2|Quantity Followed by Item Name|
|Line 3|Quantity Followed by Item Name|
|...|...|

You can add as many items as you want. The program should be pretty resilient to whitespace but if all else fails you can just modify my file.
