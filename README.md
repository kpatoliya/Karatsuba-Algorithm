# Karatsuba-Algorithm

The Karatsuba algorithm is a fast multiplication algorithm for big numbers.

For Example:
To compute the product of 12345 and 6789, where B = 10, choose m = 3. We use m right shifts for decomposing the input operands using the resulting base (Bm = 1000), as:

12345 = 12 · 1000 + 345
6789 = 6 · 1000 + 789
Only three multiplications, which operate on smaller integers, are used to compute three partial results:

z2 = 12 × 6 = 72
z0 = 345 × 789 = 272205
z1 = (12 + 345) × (6 + 789) − z2 − z0 = 357 × 795 − 72 − 272205 = 283815 − 72 − 272205 = 11538
We get the result by just adding these three partial results, shifted accordingly (and then taking carries into account by decomposing these three inputs in base 1000 like for the input operands):

result = z2 · (Bm)2 + z1 · (Bm)1 + z0 · (Bm)0, i.e.
result = 72 · 10002 + 11538 · 1000 + 272205 = 83810205.
