package crypto;

/*
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for 
 * more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import io.IO;
import io.RW;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.*;
import java.util.*;

import math.BigMathUtility;

/**
 * Paillier Cryptosystem <br><br>
 * References: <br>
 * [1] Pascal Paillier, "Public-Key Cryptosystems Based on Composite Degree Residuosity Classes," EUROCRYPT'99.
 *    URL: <a href="http://www.gemplus.com/smart/rd/publications/pdf/Pai99pai.pdf">http://www.gemplus.com/smart/rd/publications/pdf/Pai99pai.pdf</a><br>
 * 
 * [2] Paillier cryptosystem from Wikipedia. 
 *    URL: <a href="http://en.wikipedia.org/wiki/Paillier_cryptosystem">http://en.wikipedia.org/wiki/Paillier_cryptosystem</a>
 * @author Kun Liu (kunliu1@cs.umbc.edu)
 * @version 1.0
 * 
 * @modified Chen Qian (qchen@comp.hkbu.edu.hk)
 * [3] http://link.springer.com/content/pdf/10.1007%2F978-1-4419-5906-5_488.pdf
 * "Paillier Encryption and Signature Schemes"
 * 
 */
public class Paillier implements RW{

    /**
     * p and q are two large primes. 
     * lambda = lcm(p-1, q-1) = (p-1)*(q-1)/gcd(p-1, q-1).
     */
    public BigInteger p,  q,  lambda;
    /**
     * n = p*q, where p and q are two large primes.
     */
    public BigInteger n;
    /**
     * nsquare = n*n
     */
    public BigInteger nsquare;
    /**
     * a random integer in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1.
     */
    private BigInteger g;
    /**
     * number of bits of modulus
     */
    private int bitLength;

    private BigInteger eulorTotient = null;
    
    private BigInteger u = null;
    
    /**
     * Constructs an instance of the Paillier cryptosystem.
     * @param bitLengthVal number of bits of modulus
     * @param certainty The probability that the new BigInteger represents a prime number will exceed (1 - 2^(-certainty)). The execution time of this constructor is proportional to the value of this parameter.
     */
    public Paillier(int bitLengthVal, int certainty) {
        keyGeneration(bitLengthVal, certainty);
    }

    /**
     * Constructs an instance of the Paillier cryptosystem with 512 bits of modulus and at least 1-2^(-64) certainty of primes generation.
     */
    public Paillier() {
        keyGeneration(1024, 64);
    }

    public Paillier(boolean load){
    	if(load == false){
    		//new Paillier();
    		return ;
    	}
    	p = new BigInteger("7804947815828153757360229819652411239902581336788694465424613098446329573400278070343537820396291896016908012945254519909616381770357121621312154337477549");
    	q = new BigInteger("12247127755728974923191139153732636477838906492686144861614769968099366746368758706323214837421927857281933445666383738321618845728660233681677187621034241");
    	lambda = new BigInteger("23897048256811305356682045961098076786292173600974335232853047236427426860997538273435469219307501447308630774059079381490857865988655519830051295175731202351291051736924333527027538179374428889748390624825767880885082903960257661909128492544371247481923266299867219139736585751737232577347292008887134810880");
    	n = new BigInteger("95588193027245221426728183844392307145168694403897340931412188945709707443990153093741876877230005789234523096236317525963431463954622079320205180702924829457239778504826014659479126102545433300481391974142398562923398161537350416673290636930142808147446364040927488197204574242176429326744471024890497755309");
    	nsquare = new BigInteger("9137102646213891968873960082305956179850467281348997924378425537822941327328353711486252262922644444840455727525901197729954054449881680414499250710131588148276328442397092515587006832309623836117942996692050118217219959552770621784470482084525859284361578517199520103365067471771138969454007315313886974448656193939941734279189230182934062065753837306411996014007893758334981553399496819364055499244835592090097892734327146277218349751641763468959032202918273753078465610458996364688468982697036369663280854824811270106731404956468032313803254120975168652977941430279563917186236894004867711790171585042367637685481");
    	g = new BigInteger("2");
    	bitLength = 1024;
    	u =  g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
    }
    
    /**
     * Sets up the public key and private key.
     * @param bitLengthVal number of bits of modulus.
     * @param certainty The probability that the new BigInteger represents a prime number will exceed (1 - 2^(-certainty)). The execution time of this constructor is proportional to the value of this parameter.
     */
    public void keyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        /*Constructs two randomly generated positive BigIntegers that are probably prime, with the specified bitLength and certainty.*/
        p = new BigInteger(bitLength / 2, certainty, new Random());
        q = new BigInteger(bitLength / 2, certainty, new Random());

        n = p.multiply(q);
        nsquare = n.multiply(n);

        g = new BigInteger("3");
        lambda = BigMathUtility.lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
        /* check whether g is good.*/
        if (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            System.out.println("g is not good. Choose g again.");
            System.exit(1);
        }
        u =  g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
    }

    /**
     * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function explicitly requires random input r to help with encryption.
     * @param m plaintext as a BigInteger
     * @param r random plaintext to help with encryption
     * @return ciphertext as a BigInteger
     */
    public BigInteger encrypt(BigInteger m, BigInteger r) {
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
    }

    /**
     * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function automatically generates random input r (to help with encryption).
     * @param m plaintext as a BigInteger
     * @return ciphertext as a BigInteger
     */
    public BigInteger encrypt(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, new Random());
        return encrypt(m, r);

    }

    /**
     * by qchen
     * This function encrypt the message without considering r
     * */
    public BigInteger encryptWithoutR(BigInteger m){
    	return g.modPow(m, nsquare);
    }
    
    /**
     * Get Eulor Totient Function.
     * */
    public BigInteger getEulorTotient(){
    	if(eulorTotient == null){
    		eulorTotient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).multiply(n);
    	}
    	return eulorTotient;
    }
    
    public BigInteger GetG(){
    	return g;
    }
    
    /**
     * Decrypts ciphertext c. plaintext m = L(c^lambda mod n^2) * u mod n, where u = (L(g^lambda mod n^2))^(-1) mod n.
     * @param c ciphertext as a BigInteger
     * @return plaintext as a BigInteger
     */
    public BigInteger decrypt(BigInteger c) {
//        BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
    }
    
    public BigInteger[] decrypt2(BigInteger c){
    	BigInteger x = decrypt(c);
    	BigInteger y = c.multiply(g.modPow(x, n).modInverse(n)).mod(n).modPow(n.modInverse(lambda), n);
    	return new BigInteger[]{x, y};
    }
    
    
    public static void geneateconst(int bitlen){
    	Paillier paillier = new Paillier(bitlen, 64);
    	System.out.println("p = new BigInteger(" + "\"" + paillier.p + "\");");
    	System.out.println("q = new BigInteger(" + "\"" + paillier.q + "\");");
    	System.out.println("lambda = new BigInteger(" + "\"" + paillier.lambda + "\");");
    	System.out.println("n = new BigInteger(" + "\"" + paillier.n + "\");");
    	System.out.println("nsquare = new BigInteger(" + "\"" + paillier.nsquare + "\");");
    	System.out.println("g = new BigInteger(" + "\"" + paillier.g + "\");");
    	System.out.println("bitLength = " + bitlen + ";");
    	System.out.println("length of n: " + paillier.n.bitLength());
    }
    
    public void printInfo(){
		System.out.println("========== Pailler Settings =========");
		System.out.println("bitLength\t= " + bitLength);
		System.out.println("p\t\t= " + p);
		System.out.println("q\t\t= " + q);
		System.out.println("n\t\t= " + n);
		System.out.println("nsquare\t\t= " + nsquare);
		System.out.println("g\t\t= " + g);
		System.out.println("lambda\t\t= " + lambda);	
	}
    
    /**
     * main function
     * @param str intput string
     */
    public static void main(String[] str) {
        /* instantiating an object of Paillier cryptosystem*/
    	geneateconst(1024);
        Paillier paillier = new Paillier(true);
        /* instantiating two plaintext msgs*/
        BigInteger m1 = new BigInteger("20");
        BigInteger m2 = new BigInteger("60");
        /* encryption*/
        BigInteger em1 = paillier.encrypt(m1);
        BigInteger em2 = paillier.encrypt(m2);
        /* printout encrypted text*/
        System.out.println(em1);
        System.out.println(em2);
        /* printout decrypted text */
        System.out.println(paillier.decrypt(em1).toString());
        System.out.println(paillier.decrypt(em2).toString());

        /* test homomorphic properties -> D(E(m1)*E(m2) mod n^2) = (m1 + m2) mod n */
        BigInteger product_em1em2 = em1.multiply(em2).mod(paillier.nsquare);
        BigInteger sum_m1m2 = m1.add(m2).mod(paillier.n);
        System.out.println("original sum: " + sum_m1m2.toString());
        System.out.println("decrypted sum: " + paillier.decrypt(product_em1em2).toString());

        /* test homomorphic properties -> D(E(m1)^m2 mod n^2) = (m1*m2) mod n */
        BigInteger expo_em1m2 = em1.modPow(m2, paillier.nsquare);
        BigInteger prod_m1m2 = m1.multiply(m2).mod(paillier.n);
        System.out.println("original product: " + prod_m1m2.toString());
        System.out.println("decrypted product: " + paillier.decrypt(expo_em1m2).toString());
        
        //System.out.println(BigIntegerUtility.PRIME_Q.modPow(paillier.n, paillier.nsquare));

        BigInteger c1 = new BigInteger("12345");
        BigInteger c2 = new BigInteger("2");
        BigInteger[] coes1 = paillier.decrypt2(c1);
        BigInteger[] coes2 = paillier.decrypt2(c2);
        BigInteger c3 = paillier.encrypt(coes1[0].add(coes2[0]), coes1[1].multiply(coes2[1]));
        System.out.println(c3);
    }

	@Override
	public void read(DataInputStream ds) {
		// TODO Auto-generated method stub
		p = IO.readBigInteger(ds);
		q = IO.readBigInteger(ds);
		lambda = IO.readBigInteger(ds);
		n = IO.readBigInteger(ds);
		nsquare = n.multiply(n);
		g = IO.readBigInteger(ds);
		bitLength = IO.readInt(ds);
		u = IO.readBigInteger(ds);
	}

	@Override
	public void write(DataOutputStream ds) {
		// TODO Auto-generated method stub
		IO.writeBigInteger(ds, p);
		IO.writeBigInteger(ds, q);
		IO.writeBigInteger(ds, lambda);
		IO.writeBigInteger(ds, n);
		IO.writeBigInteger(ds, g);
		IO.writeInt(ds, bitLength);
		IO.writeBigInteger(ds, u);
	}
}
