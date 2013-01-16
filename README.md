## url-normalization

URL normalization (or URL canonicalization) is the process by which URLs are modified and standardized in a consistent manner. The goal of the normalization process is to transform a URL into a normalized or canonical URL so it is possible to determine if two syntactically different URLs may be equivalent.  For more detail see [Wikipedia] :http://en.wikipedia.org/wiki/URL_normalization

This java library for URL normalization (or URL canonicalization).

# Normalizations that Preserve Semantics

 * Converting the host (and scheme) to lower case:</b>
 The host (and scheme)  components of the URL are case-insensitive. This normalizer will convert them to lowercase. 
 Example: HTTP://www.Example.com/seARch → com.example/search
 * Decoding percent-encoded octets of unreserved characters:</b>
 For consistency, percent-encoded octets in the ranges of ALPHA (%41–%5A and %61–%7A), DIGIT (%30–%39), hyphen (%2D), period (%2E), underscore (%5F), or tilde (%7E) should not be created by URI producers and, when found in a URI, they will be decoded to their corresponding unreserved characters by this normalizer.
 Example: http://www.example.com/%7Eusername/ → com.example/~username/
 * Removing the default port:</b>
 The default port (port 80 for the “http” scheme) is removed from a URL. 
 Example: http://www.example.com:80/bar.html → com.example/bar.html

# Normalizations that Change Semantics

 * <b>Removing “www” as the first domain label:</b>
 Some websites operate in two Internet domains: one whose least significant label is “www” and another whose name is the result of omitting the least significant label from the name of the first. For example, http://example.com/ and http://www.example.com/ may access the same website. Many websites redirect the user from the www to the non-www address or vice versa. This normalizer determines one of these URLs redirects to the other and normalize all URLs by removing the “www” first level domain.
 Example: http://www.example.com/search → com.example/search
 * <b>Sorting the query parameters:</b>
 Some web pages use more than one query parameter in the URL. This normalizer can sort the parameters into alphabetical order (with their values), and reassemble the URL. 
 Example: http://www.example.com/display?lang=en&article=fred → com.example/display?article=fred&lang=en
 * <b>Removing the "?" when the query is empty:</b>
 When the query is empty, there may be no need for the "?". 
 Example: http://www.example.com/display? → com.example.com/display


## Quickstart

 1. Grab the sources from github: 
 
        $ git clone https://github.com/sentric/url-normalization.git
        $ cd url-normalization  
        
 2. Build:
 
        $ mvn package  
        
2. Test:

        $ mvn test
        

## Examples

        $ URL url = new URL("http://www.spiegel.de/sport/fussball/0,1518,558100,00.html");
        $ url.getNormalizedUrl(); // --> com.example/display?article=fred&lang=en
        $ url.getRepairedUrl(); // --> http://www.example.com/display?lang=en&article=fred
        $ url.getGivenInputUrl(); // http://www.example.com/display?lang=en&article=fred--> 