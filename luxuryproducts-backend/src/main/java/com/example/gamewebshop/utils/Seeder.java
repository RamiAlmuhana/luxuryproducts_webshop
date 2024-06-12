package com.example.gamewebshop.utils;

import com.example.gamewebshop.Repositorys.*;
import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.models.Category;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.Product.*;
import com.example.gamewebshop.models.Product.Enums.Colors;
import com.example.gamewebshop.models.Product.Enums.Fit;
import com.example.gamewebshop.models.Product.Enums.Sizes;
import com.example.gamewebshop.models.PromoCode;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class Seeder {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final CategoryRepository categoryRepository;
    private BrandRepository brandRepository;

    private ColorRepository colorRepository;
    private ProductImagesRepository productImagesRepository;

    private ProductVariantRepository productVariantRepository;

    private ProductVariatieRepository productVariatieRepository;
    private SizeAndFitRepository sizeAndFitRepository;
    private SizeRepository sizeRepository;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        List<CustomUser> users = this.userRepository.findAll();
        if (!users.isEmpty()) {
            return;
        }
        this.seedCategories();
        this.seedProducts();
        this.seedUsers();
        this.seedPromoCodes();
    }

    private void seedCategories() {
        Category watches = new Category("https://www.realmenrealstyle.com/wp-content/uploads/2023/08/Watch-Details.jpg", "Discover our exclusive collection of luxury watches, where timeless elegance meets exceptional craftsmanship. Each piece is meticulously designed to embody sophistication and precision, offering unparalleled style and functionality. Whether you seek a classic timepiece or a modern statement, our curated selection promises to elevate your look and reflect your refined taste.", "Watches");
        Category jewelry = new Category("https://financesonline.com/uploads/bucellati-1024x641.jpg", "Explore our exquisite jewelry collection, where artistry and elegance converge to create stunning pieces. From dazzling diamonds to vibrant gemstones, each item is masterfully crafted to enhance your natural beauty and add a touch of sophistication to any ensemble. Whether for a special occasion or everyday luxury, our selection promises to captivate and enchant.", "Jewelry");
        Category bags = new Category("https://i.etsystatic.com/42428146/r/il/4deeaf/4779037662/il_fullxfull.4779037662_lvuc.jpg", "Indulge in our luxurious collection of bags, where impeccable design meets superior craftsmanship. From timeless classics to contemporary styles, each piece is crafted with the finest materials to ensure elegance and durability. Perfect for any occasion, our curated selection offers the ultimate in sophistication and functionality, making a stylish statement wherever you go.", "Bags");
        Category clothes = new Category("https://hespokestyle.com/wp-content/uploads/2022/02/brunello-cuccinelli-luxury-menswear-brand-616x924.jpg", "Elevate your wardrobe with our luxurious clothing collection, where timeless elegance meets contemporary fashion. Each piece is crafted with exceptional attention to detail and the finest materials, ensuring both comfort and style. From sophisticated evening wear to chic everyday ensembles, our selection promises to enhance your look with unparalleled sophistication and flair.", "Clothes");

        categoryRepository.save(watches);
        categoryRepository.save(jewelry);
        categoryRepository.save(bags);
        categoryRepository.save(clothes);
    }

    private void seedProducts(){
        Category watches = categoryRepository.findByName("Watches").orElse(null);
        Category jewelry = categoryRepository.findByName("Jewelry").orElse(null);
        Category bags = categoryRepository.findByName("Bags").orElse(null);
        Category clothes = categoryRepository.findByName("Clothes").orElse(null);


        Brand cartier = new Brand("Cartier", "Cartier is renowned for its exquisite jewelry and watches, blending timeless elegance with innovative design. Known as the \"Jeweler of Kings and King of Jewelers,\" Cartier's creations are synonymous with luxury and sophistication.");
        Brand vacheron_constantin = new Brand("Vacheron Constantin", "Vacheron Constantin, one of the world's oldest watchmakers, is celebrated for its exceptional timepieces that combine meticulous craftsmanship with timeless design. Each watch is a masterpiece, reflecting over 265 years of horological excellence.");
        Brand tiffany_and_co = new Brand("Tiffany & Co", "Tiffany & Co. is an iconic American jeweler known for its stunning diamond engagement rings and exquisite sterling silver. Synonymous with elegance and style, Tiffany's creations have defined luxury since 1837.");
        Brand graff = new Brand("Graff", "Graff is synonymous with the world's most fabulous jewels, renowned for its breathtaking diamonds and exceptional craftsmanship. Each piece is a unique work of art, showcasing the natural beauty of the finest gemstones.");
        Brand chanel = new Brand("Chanel", "Chanel embodies the essence of French luxury with its timeless fashion, elegant jewelry, and iconic accessories. Renowned for its classic style and innovative designs, Chanel continues to set the standard in haute couture and luxury goods.");
        Brand hermes = new Brand("Hermes", "Hermès is celebrated for its unparalleled craftsmanship and timeless elegance, offering exquisite leather goods, ready-to-wear, and accessories. Known for its iconic Birkin and Kelly bags, Hermès epitomizes luxury and refinement.");
        Brand gucci = new Brand("Gucci", "Gucci is a symbol of contemporary luxury and Italian craftsmanship, known for its bold designs and distinctive motifs. From fashion to accessories, Gucci's creations are both innovative and timeless, making a statement in the world of luxury.");

        Color black = new Color(Colors.BLACK);
        Color white = new Color(Colors.WHITE);
        Color silver = new Color(Colors.SILVER);
        Color gold = new Color(Colors.GOLD);
        Color red = new Color(Colors.RED);
        Color green = new Color(Colors.GREEN);
        Color brown = new Color(Colors.BROWN);

        Size xs = new Size(Sizes.XS);
        Size s = new Size(Sizes.S);
        Size m = new Size(Sizes.M);
        Size l = new Size(Sizes.L);
        Size xl = new Size(Sizes.XL);
        Size xxl = new Size(Sizes.XXL);

        SizeAndFit classic = new SizeAndFit(Fit.Classic_Fit);
        SizeAndFit relaxed = new SizeAndFit(Fit.Relaxed_Fit);
        SizeAndFit slim = new SizeAndFit(Fit.Slim_Fit);
        SizeAndFit regular = new SizeAndFit(Fit.Regular_Fit);
        SizeAndFit loose = new SizeAndFit(Fit.Loose_Fit);
        SizeAndFit tailord = new SizeAndFit(Fit.Tailored_Fit);



        Product watch1 = new Product(cartier,"TANK FRANÇAISE WATCH", "Made in France", watches);
        Product watch2 = new Product(cartier, "TANK LOUIS CARTIER WATCH", "Made in Italy", watches);
        Product watch3 = new Product(vacheron_constantin, "VACHERON CONSTANTIN GENEVE", "Made in Spain", watches);
        Product jewelry1 = new Product(tiffany_and_co, "VICTORIA`S EARRINGS", "Made in Croatia", jewelry);
        Product jewelry2 = new Product(tiffany_and_co, "SIXTEEN STONE RING", "Made in Luxembourg", jewelry);
        Product jewelry3 = new Product(graff, "Spiral Pavé Diamond Band", "Made in France", jewelry);
        Product bags1 = new Product(chanel, "CAMERA TAS", "Made in the Netherlands", bags);
        Product bags2 = new Product(hermes, "Herbag Zip cabine bag", "Made in Spain", bags);
        Product clothes1 = new Product(gucci, "COTTON JERSEY T-SHIRT WITH GUCCI PRINT", "Made in France", clothes);
        Product clothes2 = new Product(gucci, "GUCCI SILK JACQUARD SHIRT AND BRA SET", "Made in Portugal", clothes);



        ProductVariant watch1v1 = new ProductVariant(watch1,"Tank Française watch, quartz movement. Steel case. Faceted crown in steel decorated with a synthetic cabochon-shaped spinel. Silvered sunray dial, blued-steel sword-shaped hands, sapphire crystal. Steel bracelet. Case dimensions: 25.7 mm x 21.2 mm, thickness: 6.8 mm. Water-resistant to 3 bar (approx. 30 metres).", silver, 4000L, regular );
        ProductVariant watch1v2 = new ProductVariant(watch1, "Tank Française watch, quartz movement. Case in yellow gold 750/1000. Faceted crown in yellow gold 750/1000 decorated with a sapphire cabochon. Golden sunray dial, blued-steel sword-shaped hands, sapphire crystal. Bracelet in yellow gold 750/1000. Dimensions: 32 mm x 27 mm, thickness: 7.1 mm. water-resistant to 3 bar (approx. 30 metres).", gold, 28000L, classic);

        ProductVariant watch2v1 = new ProductVariant(watch2,"Tank Louis Cartier watch, quartz movement. Case in yellow gold 750/1000, beaded crown set with a sapphire cabochon, silvered grained dial, sword-shaped hands in blued steel, mineral crystal, alligator-skin strap with ardillon buckle in yellow gold 750/1000. Case dimensions: 29.5 mm x 22 mm, 6.35 mm thick. Water-resistant to 30 metres/100 feet.", brown, 11200L, tailord);
        ProductVariant watch2v2 = new ProductVariant(watch2, "Tank Louis Cartier watch, quartz movement. Case in yellow gold 750/1000, beaded crown set with a sapphire, grained silvered dial, blued-steel sword-shaped hands, mineral crystal, strap in dark grey alligator skin, ardillon buckle in yellow gold 750/1000. Case dimensions: 33.7 mm x 25.5 mm, thickness: 6.3 mm. Water-resistant to 30 meters/100 feet.", black, 12500L, tailord);

        ProductVariant watch3v1 = new ProductVariant(watch3, "A tribute to the spirit of travel, this 18K white gold watch is distinguished by its bezel set with 60 baguette-cut diamonds. It houses an ultra-slim movement just 5.56 millimeters thick and a tourbillon cage inspired by the Maltese cross. Fitted with a 22K peripheral rotor revealing the entire movement through the openworked caseback, this exceptional caliber offers a power reserve of more than 3 days. Thanks to its clasp and system of three easily interchangeable straps – 18K white gold, leather, and rubber – the watch can be personalized in accordance with the owner’s wishes.", silver, 30000L, tailord);
        ProductVariant watch3v2 = new ProductVariant(watch3, "A tribute to the spirit of travel, this 18K white gold watch is distinguished by its bezel set with 60 baguette-cut diamonds. It houses an ultra-slim movement just 5.56 millimeters thick and a tourbillon cage inspired by the Maltese cross. Fitted with a 22K peripheral rotor revealing the entire movement through the openworked caseback, this exceptional caliber offers a power reserve of more than 3 days. Thanks to its clasp and system of three easily interchangeable straps – 18K white gold, leather, and rubber – the watch can be personalized in accordance with the owner’s wishes.", gold, 20000L, regular);


        ProductVariant jewelry1v1 = new ProductVariant(jewelry1, """
                Inspired by the fire and radiance of our superlative diamonds, Tiffany Victoria uses a unique combination of cuts for a distinctly romantic sensibility. The beautiful shape of these classic diamond earrings allows the stones to play off of each other's glorious sheen.

                18k rose gold with marquise diamonds
                """
                , brown, 5000L, classic );
        ProductVariant jewelry1v2 = new ProductVariant(jewelry1, "Inspired by the fire and radiance of our superlative diamonds, Tiffany Victoria uses a unique combination of cuts for a distinctly romantic sensibility. The beautiful shape of these classic diamond earrings allows the stones to play off of each other's glorious sheen.\n" +
                "\n" +
                "Platinum with marquise diamonds", silver, 7000L, classic);

        ProductVariant jewelry2v1 = new ProductVariant(jewelry2, "Jean Schlumberger’s visionary creations are among the world’s most intricate designs. Brilliant diamonds alternate with golden X's to create this dazzling design.\n" +
                "\n" +
                "18k rose gold and platinum with round brilliant diamonds", brown, 14500L, classic);
        ProductVariant jewelry2v2 = new ProductVariant(jewelry2, "Jean Schlumberger’s visionary creations are among the world’s most intricate designs. Brilliant diamonds alternate with platinum X's to create this dazzling design.\n" +
                "\n" +
                "Platinum with round brilliant diamonds", silver, 16000L, classic);

        ProductVariant jewelry3v1 = new ProductVariant(jewelry3, "Featuring eternally twisting streams of pavé diamonds, our 2.2mm Spiral band in white gold forms an unbreakable circle of scintillation around the finger. Symbolic of everlasting love, the delicate yet dynamic design is endlessly enchanting.\n", silver, 5900L, slim);
        ProductVariant jewelry3v2 = new ProductVariant(jewelry3, "Featuring eternally twisting streams of pavé diamonds, our 2.2mm Spiral band in rose gold forms an unbreakable circle of scintillation around the finger. Symbolic of everlasting love, the delicate yet dynamic design is endlessly enchanting. The Graff Spiral collection is a modern classic filled with delicate gold and pavé diamond jewels that are designed to be stacked and mixed and matched, alongside a range of captivating timepieces.", gold, 5900L, slim);

        ProductVariant bags1v1 = new ProductVariant(bags1,"The White Chanel Camera Bag is a chic and versatile accessory, crafted from premium leather and featuring the iconic Chanel quilting. This stylish bag offers a compact yet spacious design with multiple compartments, perfect for carrying essentials with elegance. Adorned with the signature CC logo and a delicate chain strap, it effortlessly combines functionality and timeless sophistication.", white, 9000L, regular );
        ProductVariant bags1v2 = new ProductVariant(bags1, "The Black Chanel Camera Bag exudes timeless elegance and versatility, crafted from luxurious quilted leather. This sophisticated accessory features multiple compartments for effortless organization, adorned with the iconic CC logo and a classic chain strap. Perfect for any occasion, it combines practicality with Chanel's signature style.", black, 9000L, regular);

        ProductVariant bags2v1 = new ProductVariant(bags2, "Travel bag in Militaire canvas and Hunter cowhide\n" +
                "\n" +
                "- Palladium-plated Clou de Selle closure\n" +
                "- Removable interior zipped pouch\n" +
                "- Exterior back zipped pocket\n" +
                "- Collapsible handle\n" +
                "- Bag strap\n" +
                "- Can be carried by hand or worn over the shoulder", green, 4200L, classic );
        ProductVariant bags2v2 = new ProductVariant(bags2, "Travel bag in Militaire canvas and Hunter cowhide\n" +
                "\n" +
                "- Palladium-plated Clou de Selle closure\n" +
                "- Removable interior zipped pouch\n" +
                "- Exterior back zipped pocket\n" +
                "- Collapsible handle\n" +
                "- Bag strap\n" +
                "- Can be carried by hand or worn over the shoulder", black, 4200L, classic);

        ProductVariant clothes1v1 = new ProductVariant(clothes1, "Inspired by the summer spirit and beach clubs on the Italian coast, this item is part of Gucci Lido. Emblematic codes of the House and refined patterns are reinterpreted in contemporary ways for the Pre-Fall collection. Presented in white cotton jersey, this T-shirt is defined by a Gucci Double G Firenze 1921 faded print.\n" +
                "\n" +
                "White cotton jersey\n" +
                "Blue Gucci Double G Firenze 1921 faded print\n" +
                "Crewneck\n" +
                "Short sleeves\n" +
                "Fabric: 100% Cotton.", white, 420L, relaxed);
        ProductVariant clothes1v2 = new ProductVariant(clothes1, "Designs by London-based artist and illustrator Hattie Stewart lend a vibrant and playful touch to the House's Pre-Fall 2024 collection. Her unique and bold style brings the Gucci logo to life alongside other fun symbols, appearing in the form of prints, patches and embroideries. Enriching ready-to-wear pieces and accessories, the motifs reference art, design and fashion, continually challenging conventional expectations.\n" +
                "\n" +
                "Red cotton jersey\n" +
                "Gucci eyes embroidery\n" +
                "Artwork by Hattie Stewart\n" +
                "Crewneck\n" +
                "Short sleeves\n" +
                "Fabric: 100% Cotton.\n" +
                "Embroidery: 100% Polyester.", red, 400L, loose);

        ProductVariant clothes2v1 = new ProductVariant(clothes2, "The Pre-Fall selections spotlight essential silhouettes with emblematic details from daywear to eveningwear. This elegant shirt reveals a relaxed silhouette presented in a Gucci silk jacquard. A detachable self-tie bra offers a tonal base for every look.\n" +
                "\n" +
                "Black Gucci silk jacquard\n" +
                "Point collar\n" +
                "Long sleeves\n" +
                "Detachable self-tie bra\n" +
                "Button closure\n" +
                "Shirt: Fabric: 100% Silk.\n" +
                "Woven Bikini top: Fabric: 100% Silk.", black, 1350L, tailord);
        ProductVariant clothes2v2 = new ProductVariant(clothes2, "Inspired by the summer spirit and beach clubs on the Italian coast, this item is part of Gucci Lido. The Pre-Fall selections spotlight essential silhouettes with emblematic details from daywear to eveningwear. This elegant shirt reveals a relaxed silhouette presented in a Gucci silk jacquard. A detachable self-tie bra offers a tonal base for every look.\n" +
                "\n" +
                "Ivory Gucci silk jacquard\n" +
                "Point collar\n" +
                "Long sleeves\n" +
                "Detachable self-tie bra\n" +
                "Button closure\n" +
                "Shirt: Fabric: 100% Silk.\n" +
                "Woven Bikini top: Fabric: 100% Silk.\n", white, 1350L, tailord);




        ProductVariant[] productVariants = {watch1v1, watch1v2, watch2v1, watch2v2, watch3v1, watch3v2, jewelry1v1, jewelry1v2, jewelry2v1, jewelry2v2, jewelry3v1, jewelry3v2, bags1v1, bags1v2, bags2v1, bags2v2, clothes1v1, clothes1v2, clothes2v1, clothes2v2};


        ProductImages image1watch1v1 = new ProductImages(watch1v1,"https://www.cartier.com/variants/images/1647597304509671/img1/w260_tpadding12.jpg");
        ProductImages image2watch1v1 = new ProductImages(watch1v1,"https://www.cartier.com/variants/images/1647597304509671/img2/w260_tpadding12.jpg");
        ProductImages image3watch1v1 = new ProductImages(watch1v1,"https://www.cartier.com/variants/images/1647597304509671/img8/w260_tpadding12.jpg");
        ProductImages image4watch1v1 = new ProductImages(watch1v1,"https://www.cartier.com/variants/images/1647597304509671/img10/w260_tpadding12.jpg");

        ProductImages image1watch1v2 = new ProductImages(watch1v2, "https://www.cartier.com/variants/images/1647597304509656/img1/w260_tpadding12.jpg");
        ProductImages image2watch1v2 = new ProductImages(watch1v2, "https://www.cartier.com/variants/images/1647597304509656/img2/w260_tpadding12.jpg");
        ProductImages image3watch1v2 = new ProductImages(watch1v2, "https://www.cartier.com/variants/images/1647597304509656/img9/w260_tpadding12.jpg");
        ProductImages image4watch1v2 = new ProductImages(watch1v2, "https://www.cartier.com/variants/images/1647597304509656/img11/w260_tpadding12.jpg");

        ProductImages image1watch2v1 = new ProductImages(watch2v1, "https://www.cartier.com/variants/images/44733502651459498/img1/w260_tpadding12.jpg");
        ProductImages image2watch2v1 = new ProductImages(watch2v1, "https://www.cartier.com/variants/images/44733502651459498/img7/w260_tpadding12.jpg");
        ProductImages image3watch2v1 = new ProductImages(watch2v1, "https://www.cartier.com/variants/images/44733502651459498/img9/w260_tpadding12.jpg");
        ProductImages image4watch2v1 = new ProductImages(watch2v1, "https://www.cartier.com/variants/images/44733502651459498/img11/w260_tpadding12.jpg");

        ProductImages image1watch2v2 = new ProductImages(watch2v2, "https://www.cartier.com/variants/images/13452677149801337/img1/w528_tpadding12.jpg");
        ProductImages image2watch2v2 = new ProductImages(watch2v2, "https://www.cartier.com/variants/images/13452677149801337/img4/w260_tpadding12.jpg");
        ProductImages image3watch2v2 = new ProductImages(watch2v2, "https://www.cartier.com/variants/images/13452677149801337/img7/w260_tpadding12.jpg");
        ProductImages image4watch2v2 = new ProductImages(watch2v2, "https://www.cartier.com/variants/images/13452677149801337/img10/w260_tpadding12.jpg");

        ProductImages image1watch3v1 = new ProductImages(watch3v1, "https://www.vacheron-constantin.com/dam/rcq/vac/Wa/GA/pL/Nb/pk/Kb/O6/Zk/NH/1o/qA/WaGApLNbpkKbO6ZkNH1oqA.png.transform.vacprodcardhd.png");
        ProductImages image2watch3v1 = new ProductImages(watch3v1, "https://www.vacheron-constantin.com/dam/rcq/vac/dr/-O/tQ/lZ/sU/aI/1U/sb/3X/oh/Dw/dr-OtQlZsUaI1Usb3XohDw.png.transform.vacprodgalleryhd.png");
        ProductImages image3watch3v1 = new ProductImages(watch3v1, "https://www.vacheron-constantin.com/dam/rcq/vac/c6/KF/XB/-U/7k/W3/9-/Ql/sa/GG/TA/c6KFXB-U7kW39-QlsaGGTA.png.transform.vacprodgalleryhd.png");
        ProductImages image4watch3v1 = new ProductImages(watch3v1, "https://www.vacheron-constantin.com/dam/rcq/vac/1s/-n/2s/ni/ME/ic/Ub/0Q/XP/XZ/9A/1s-n2sniMEicUb0QXPXZ9A.png.transform.vacprodgalleryhd.png");

        ProductImages image1watch3v2 = new ProductImages(watch3v2, "https://www.vacheron-constantin.com/dam/rcq/vac/G3/V0/11/2K/Sm/y8/jK/ZU/sT/C4/PA/G3V0112KSmy8jKZUsTC4PA.png.transform.vacprodcardhd.png");
        ProductImages image2watch3v2 = new ProductImages(watch3v2, "https://www.vacheron-constantin.com/dam/rcq/vac/E5/5q/BQ/lV/Qf/KA/El/_C/oK/BW/6Q/E55qBQlVQfKAEl_CoKBW6Q.png.transform.vacprodgalleryhd.png");
        ProductImages image3watch3v2 = new ProductImages(watch3v2, "https://www.vacheron-constantin.com/dam/rcq/vac/7m/jw/l4/jA/TL/uw/k4/jj/eN/pW/zQ/7mjwl4jATLuwk4jjeNpWzQ.png.transform.vacprodgalleryhd.png");
        ProductImages image4watch3v2 = new ProductImages(watch3v2, "https://www.vacheron-constantin.com/dam/rcq/vac/XA/wV/T2/tt/Qs/mU/8h/T-/g8/I8/5g/XAwVT2ttQsmU8hT-g8I85g.png.transform.vacprodgalleryhd.png");

        ProductImages image1jewelry1v1 = new ProductImages(jewelry1v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-32948278_1016114_ED.jpg?&op_usm=2.0,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image2jewelry1v1 = new ProductImages(jewelry1v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-32948278_1016112_AV_1.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image3jewelry1v1 = new ProductImages(jewelry1v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-32948278_1016113_AV_2.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image4jewelry1v1 = new ProductImages(jewelry1v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-32948278_1016115_SV_1.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");

        ProductImages image1jewelry1v2 = new ProductImages(jewelry1v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-23954168_997901_ED_M.jpg?&op_usm=2.0,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp" );
        ProductImages image2jewelry1v2 = new ProductImages(jewelry1v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-63106402_1027002_AV_1.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image3jewelry1v2 = new ProductImages(jewelry1v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-63106402_1027003_AV_2.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image4jewelry1v2 = new ProductImages(jewelry1v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-victoriaearrings-23954168_1028075_SV_1.jpg?&op_usm=2.0,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");

        ProductImages image1jewelry2v1 = new ProductImages(jewelry2v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-28749058_1029141_ED_M.jpg?&op_usm=1.75,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp" );
        ProductImages image2jewelry2v1 = new ProductImages(jewelry2v1, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-28749058_1029142_SV_1_M.jpg?&op_usm=1.75,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");


        ProductImages image1jewelry2v2 = new ProductImages(jewelry2v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186555_1039864_ED.jpg?&op_usm=1.75,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp" );
        ProductImages image2jewelry2v2 = new ProductImages(jewelry2v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186555_1032159_SV_1.jpg?&op_usm=1.75,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image3jewelry2v2 = new ProductImages(jewelry2v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186504_1043290_AV_3_M.jpg?&op_usm=1.75,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");
        ProductImages image4jewelry2v2 = new ProductImages(jewelry2v2, "https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186555_1032156_AV_1.jpg?&op_usm=1.75,1.0,6.0&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp");

        ProductImages image1jewelry3v1 = new ProductImages(jewelry3v1, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dw759189fc/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR558_GR63782_Hero_1.jpg?sw=1000&sh=1000" );
        ProductImages image2jewelry3v1 = new ProductImages(jewelry3v1, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dw45dd0e68/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR558_Model_2.jpg?sw=1000&sh=1000");
        ProductImages image3jewelry3v1 = new ProductImages(jewelry3v1, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dwfa86e7c1/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR558_model_3.jpg?sw=1000&sh=1000");
        ProductImages image4jewelry3v1 = new ProductImages(jewelry3v1, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dwfea4db58/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR558_Side_5.jpg?sw=1000&sh=1000");

        ProductImages image1jewelry3v2 = new ProductImages(jewelry3v2, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dwdcf6d2bb/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR598_GR64495_Hero_1.jpg?sw=1000&sh=1000" );
        ProductImages image2jewelry3v2 = new ProductImages(jewelry3v2, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dw68b533aa/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR598_Model_2.jpg?sw=1000&sh=1000" );
        ProductImages image3jewelry3v2 = new ProductImages(jewelry3v2, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dwbbff9bc3/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR598_Model_3.jpg?sw=1000&sh=1000" );
        ProductImages image4jewelry3v2 = new ProductImages(jewelry3v2, "https://www.graff.com/dw/image/v2/BFNT_PRD/on/demandware.static/-/Sites-master-catalog/default/dw92dcf63b/sfcc-graff-staging/i/m/a/g/e/images_hi_res_RGR558ALL_RGR598_Side_6.jpg?sw=1000&sh=1000");

        ProductImages image1bags1v1 = new ProductImages(bags1v1, "https://www.chanel.com/images//t_one///q_auto:good,f_autoplus,fl_lossy,dpr_1.1/w_840/camera-bag-white-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue1-as4817b1625510601-9538352840734.jpg");
        ProductImages image2bags1v1 = new ProductImages(bags1v1, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-white-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue2-as4817b1625510601-9538352873502.jpg");
        ProductImages image3bags1v1 = new ProductImages(bags1v1, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-white-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue3-as4817b1625510601-9538352906270.jpg" );
        ProductImages image4bags1v1 = new ProductImages(bags1v1, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-white-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue4-as4817b1625510601-9538352971806.jpg" );

        ProductImages image1bags1v2 = new ProductImages(bags1v2, "https://www.chanel.com/images//t_one///q_auto:good,f_autoplus,fl_lossy,dpr_1.1/w_840/camera-bag-black-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue1-as4817b1625594305-9538351988766.jpg" );
        ProductImages image2bags1v2 = new ProductImages(bags1v2, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-black-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue2-as4817b1625594305-9538351890462.jpg" );
        ProductImages image3bags1v2 = new ProductImages(bags1v2, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-black-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue3-as4817b1625594305-9538351923230.jpg" );
        ProductImages image4bags1v2 = new ProductImages(bags1v2, "https://www.chanel.com/images//t_one///q_auto:good,f_auto,fl_lossy,dpr_1.1/w_840/camera-bag-black-lambskin-gold-tone-metal-lambskin-gold-tone-metal-packshot-artistique-vue4-as4817b1625594305-9538352218142.jpg" );

        ProductImages image1bags2v1 = new ProductImages(bags2v1, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAL-front-wm-1-0-0-800-800_g.jpg" );
        ProductImages image2bags2v1 = new ProductImages(bags2v1, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAL-side-wm-4-0-0-800-800_g.jpg");
        ProductImages image3bags2v1 = new ProductImages(bags2v1, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAL-above-wm-5-0-0-800-800_g.jpg");
        ProductImages image4bags2v1 = new ProductImages(bags2v1, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAL-back-wm-6-0-0-800-800_g.jpg");

        ProductImages image1bags2v2 = new ProductImages(bags2v2, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAA-front-1-300-0-800-800_g.jpg" );
        ProductImages image2bags2v2 = new ProductImages(bags2v2, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAA-side-2-300-0-800-800_g.jpg");
        ProductImages image3bags2v2 = new ProductImages(bags2v2, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAA-back-3-300-0-800-800_g.jpg");
        ProductImages image4bags2v2 = new ProductImages(bags2v2, "https://assets.hermes.com/is/image/hermesproduct/herbag-zip-cabine-bag--077787CKAA-above-4-300-0-800-800_g.jpg");

        ProductImages image1clothes1v1 = new ProductImages(clothes1v1, "https://media.gucci.com/style/DarkGray_Center_0_0_800x800/1712855753/785345_XJGKJ_9034_001_100_0000_Light-Cotton-jersey-T-shirt-with-Gucci-print.jpg" );
        ProductImages image2clothes1v1 = new ProductImages(clothes1v1, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712855754/785345_XJGKJ_9034_002_100_0000_Light-Cotton-jersey-T-shirt-with-Gucci-print.jpg");
        ProductImages image3clothes1v1 = new ProductImages(clothes1v1, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712855755/785345_XJGKJ_9034_004_100_0000_Light-Cotton-jersey-T-shirt-with-Gucci-print.jpg");
        ProductImages image4clothes1v1 = new ProductImages(clothes1v1, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712855756/785345_XJGKJ_9034_005_100_0000_Light-Cotton-jersey-T-shirt-with-Gucci-print.jpg");

        ProductImages image1clothes1v2 = new ProductImages(clothes1v2, "https://media.gucci.com/style/DarkGray_Center_0_0_800x800/1708967885/785345_XJGIL_6429_001_100_0000_Light-Cotton-jersey-T-shirt-with-embroidery.jpg");
        ProductImages image2clothes1v2 = new ProductImages(clothes1v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1708967886/785345_XJGIL_6429_002_100_0000_Light-Cotton-jersey-T-shirt-with-embroidery.jpg");
        ProductImages image3clothes1v2 = new ProductImages(clothes1v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1708967887/785345_XJGIL_6429_003_100_0000_Light-Cotton-jersey-T-shirt-with-embroidery.jpg");
        ProductImages image4clothes1v2 = new ProductImages(clothes1v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1708967888/785345_XJGIL_6429_004_100_0000_Light-Cotton-jersey-T-shirt-with-embroidery.jpg");

        ProductImages image1clothes2v1 = new ProductImages(clothes2v1, "https://media.gucci.com/style/DarkGray_Center_0_0_800x800/1709891132/788991_ZAQRU_1000_001_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg" );
        ProductImages image2clothes2v1 = new ProductImages(clothes2v1, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712595704/788991_ZAQRU_1000_002_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg");
        ProductImages image3clothes2v1 = new ProductImages(clothes2v1, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1709891137/788991_ZAQRU_1000_012_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg");

        ProductImages image1clothes2v2 = new ProductImages(clothes2v2, "https://media.gucci.com/style/DarkGray_Center_0_0_800x800/1712150231/788991_ZAQRU_9200_001_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg" );
        ProductImages image2clothes2v2 = new ProductImages(clothes2v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712150231/788991_ZAQRU_9200_002_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg");
        ProductImages image3clothes2v2 = new ProductImages(clothes2v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712150232/788991_ZAQRU_9200_003_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg");
        ProductImages image4clothes2v2 = new ProductImages(clothes2v2, "https://media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1712150233/788991_ZAQRU_9200_004_100_0000_Light-Gucci-silk-jacquard-shirt-and-bra-set.jpg");

        ProductImages[] productImages = {image1watch1v1, image2watch1v1, image3watch1v1,image4watch1v1, image1watch1v2 , image2watch1v2 , image3watch1v2 , image4watch1v2 , image1watch2v1 , image2watch2v1 , image3watch2v1 , image4watch2v1 , image1watch2v2 ,
                image2watch2v2 , image3watch2v2 , image4watch2v2 , image1watch3v1 , image2watch3v1 , image3watch3v1 , image4watch3v1 , image1watch3v2 , image2watch3v2 , image3watch3v2 , image4watch3v2 , image1jewelry1v1, image2jewelry1v1 , image3jewelry1v1, image4jewelry1v1, image1jewelry1v2, image2jewelry1v2, image3jewelry1v2, image4jewelry1v2, image1jewelry2v1, image2jewelry2v1, image1jewelry2v2, image2jewelry2v2, image3jewelry2v2, image4jewelry2v2, image1jewelry3v1, image2jewelry3v1, image3jewelry3v1, image4jewelry3v1, image1jewelry3v2, image2jewelry3v2, image3jewelry3v2, image4jewelry3v2,
                image1bags1v1 , image2bags1v1 , image3bags1v1 ,  image4bags1v1, image1bags1v2 , image2bags1v2 , image3bags1v2 , image4bags1v2 , image1bags2v1 , image2bags2v1 , image3bags2v1 , image4bags2v1 , image1bags2v2 , image2bags2v2 , image3bags2v2 , image4bags2v2 , image1clothes1v1, image2clothes1v1, image3clothes1v1, image4clothes1v1, image1clothes1v2, image2clothes1v2, image3clothes1v2, image4clothes1v2, image1clothes2v1, image2clothes2v1, image3clothes2v1, image1clothes2v2, image2clothes2v2, image3clothes2v2, image4clothes2v2  };


        ProductVariatie productVariatie1Watch1v1 = new ProductVariatie(watch1v1, s, 50L);
        ProductVariatie productVariatie2Watch1v1 = new ProductVariatie(watch1v1, m, 50L);
        ProductVariatie productVariatie3Watch1v1 = new ProductVariatie(watch1v1, l, 50L);

        ProductVariatie productVariatie1Watch1v2 = new ProductVariatie(watch1v2, s, 50L);
        ProductVariatie productVariatie2Watch1v2 = new ProductVariatie(watch1v2, m,50L);
        ProductVariatie productVariatie3Watch1v2 = new ProductVariatie(watch1v2, l,50L);

        ProductVariatie productVariatie1Watch2v1 = new ProductVariatie(watch2v1, s,50L);
        ProductVariatie productVariatie2Watch2v1 = new ProductVariatie(watch2v1, m,50L);
        ProductVariatie productVariatie3Watch2v1 = new ProductVariatie(watch2v1, l,50L);

        ProductVariatie productVariatie1Watch2v2 = new ProductVariatie(watch2v2, s,50L);
        ProductVariatie productVariatie2Watch2v2 = new ProductVariatie(watch2v2, m,50L );
        ProductVariatie productVariatie3Watch2v2 = new ProductVariatie(watch2v2, l,50L);

        ProductVariatie productVariatie1Watch3v1 = new ProductVariatie(watch3v1, s,50L );
        ProductVariatie productVariatie2Watch3v1 = new ProductVariatie(watch3v1, m,50L);
        ProductVariatie productVariatie3Watch3v1 = new ProductVariatie(watch3v1, l,50L);

        ProductVariatie productVariatie1Watch3v2 = new ProductVariatie(watch3v2, s,50L);
        ProductVariatie productVariatie2Watch3v2 = new ProductVariatie(watch3v2, m,50L);
        ProductVariatie productVariatie3Watch3v2 = new ProductVariatie(watch3v2, l,50L );


        ProductVariatie productVariatie1jewelry1v1 = new ProductVariatie(jewelry1v1, s, 50L);
        ProductVariatie productVariatie2jewelry1v1 = new ProductVariatie(jewelry1v1, m, 50L);
        ProductVariatie productVariatie3jewelry1v1 = new ProductVariatie(jewelry1v1, l, 50L);

        ProductVariatie productVariatie1jewelry1v2 = new ProductVariatie(jewelry1v2, s, 50L);
        ProductVariatie productVariatie2jewelry1v2 = new ProductVariatie(jewelry1v2, m,50L);
        ProductVariatie productVariatie3jewelry1v2 = new ProductVariatie(jewelry1v2, l,50L);

        ProductVariatie productVariatie1jewelry2v1 = new ProductVariatie(jewelry2v1, s,50L);
        ProductVariatie productVariatie2jewelry2v1 = new ProductVariatie(jewelry2v1, m,50L);
        ProductVariatie productVariatie3jewelry2v1 = new ProductVariatie(jewelry2v1, l,50L);

        ProductVariatie productVariatie1jewelry2v2 = new ProductVariatie(jewelry2v2, s,50L);
        ProductVariatie productVariatie2jewelry2v2 = new ProductVariatie(jewelry2v2, m,50L );
        ProductVariatie productVariatie3jewelry2v2 = new ProductVariatie(jewelry2v2, l,50L);

        ProductVariatie productVariatie1jewelry3v1 = new ProductVariatie(jewelry3v1, s,50L );
        ProductVariatie productVariatie2jewelry3v1 = new ProductVariatie(jewelry3v1, m,50L);
        ProductVariatie productVariatie3jewelry3v1 = new ProductVariatie(jewelry3v1, l,50L);

        ProductVariatie productVariatie1jewelry3v2 = new ProductVariatie(jewelry3v2, s,50L);
        ProductVariatie productVariatie2jewelry3v2 = new ProductVariatie(jewelry3v2, m,50L);
        ProductVariatie productVariatie3jewelry3v2 = new ProductVariatie(jewelry3v2, l,50L );



        ProductVariatie productVariatie1bags1v1 = new ProductVariatie(bags1v1, s, 50L);
        ProductVariatie productVariatie2bags1v1 = new ProductVariatie(bags1v1, m, 50L);
        ProductVariatie productVariatie3bags1v1 = new ProductVariatie(bags1v1, l, 50L);

        ProductVariatie productVariatie1bags1v2 = new ProductVariatie(bags1v2, s, 50L);
        ProductVariatie productVariatie2bags1v2 = new ProductVariatie(bags1v2, m,50L);
        ProductVariatie productVariatie3bags1v2 = new ProductVariatie(bags1v2, l,50L);

        ProductVariatie productVariatie1bags2v1 = new ProductVariatie(bags2v1, s,50L);
        ProductVariatie productVariatie2bags2v1 = new ProductVariatie(bags2v1, m,50L);
        ProductVariatie productVariatie3bags2v1 = new ProductVariatie(bags2v1, l,50L);

        ProductVariatie productVariatie1bags2v2 = new ProductVariatie(bags2v2, s,50L);
        ProductVariatie productVariatie2bags2v2 = new ProductVariatie(bags2v2, m,50L );
        ProductVariatie productVariatie3bags2v2 = new ProductVariatie(bags2v2, l,50L);

        ProductVariatie productVariatie1clothes1v1 = new ProductVariatie(clothes1v1, xs, 50L);
        ProductVariatie productVariatie2clothes1v1 = new ProductVariatie(clothes1v1, s, 50L);
        ProductVariatie productVariatie3clothes1v1 = new ProductVariatie(clothes1v1, m, 50L);
        ProductVariatie productVariatie4clothes1v1 = new ProductVariatie(clothes1v1, l, 50L);
        ProductVariatie productVariatie5clothes1v1 = new ProductVariatie(clothes1v1, xl, 50L);
        ProductVariatie productVariatie6clothes1v1 = new ProductVariatie(clothes1v1, xxl, 50L);


        ProductVariatie productVariatie1clothes1v2 = new ProductVariatie(clothes1v2, xs, 50L);
        ProductVariatie productVariatie2clothes1v2 = new ProductVariatie(clothes1v2, s,50L);
        ProductVariatie productVariatie3clothes1v2 = new ProductVariatie(clothes1v2, m,50L);
        ProductVariatie productVariatie4clothes1v2 = new ProductVariatie(clothes1v2, l, 50L);
        ProductVariatie productVariatie5clothes1v2 = new ProductVariatie(clothes1v2, xl, 50L);
        ProductVariatie productVariatie6clothes1v2 = new ProductVariatie(clothes1v2, xxl, 50L);


        ProductVariatie productVariatie1clothes2v1 = new ProductVariatie(clothes2v1, xs,50L);
        ProductVariatie productVariatie2clothes2v1 = new ProductVariatie(clothes2v1, s,50L);
        ProductVariatie productVariatie3clothes2v1 = new ProductVariatie(clothes2v1, m,50L);
        ProductVariatie productVariatie4clothes2v1 = new ProductVariatie(clothes2v1, l, 50L);
        ProductVariatie productVariatie5clothes2v1 = new ProductVariatie(clothes2v1, xl, 50L);
        ProductVariatie productVariatie6clothes2v1 = new ProductVariatie(clothes2v1, xxl, 50L);


        ProductVariatie productVariatie1clothes2v2 = new ProductVariatie(clothes2v2, xs,50L);
        ProductVariatie productVariatie2clothes2v2 = new ProductVariatie(clothes2v2, s,50L );
        ProductVariatie productVariatie3clothes2v2 = new ProductVariatie(clothes2v2, m,50L);
        ProductVariatie productVariatie4clothes2v2 = new ProductVariatie(clothes2v2, l, 50L);
        ProductVariatie productVariatie5clothes2v2 = new ProductVariatie(clothes2v2, xl, 50L);
        ProductVariatie productVariatie6clothes2v2 = new ProductVariatie(clothes2v2, xxl, 50L);

        ProductVariatie [] productVariaties = {productVariatie1Watch1v1 , productVariatie2Watch1v1 , productVariatie3Watch1v1 , productVariatie1Watch1v2 , productVariatie2Watch1v2 , productVariatie3Watch1v2 , productVariatie1Watch2v1 , productVariatie2Watch2v1 , productVariatie3Watch2v1 , productVariatie1Watch2v2 , productVariatie2Watch2v2 , productVariatie3Watch2v2 , productVariatie1Watch3v1 , productVariatie2Watch3v1 , productVariatie3Watch3v1 , productVariatie1Watch3v2 , productVariatie2Watch3v2 , productVariatie3Watch3v2 , productVariatie1jewelry1v1 , productVariatie2jewelry1v1 , productVariatie3jewelry1v1 , productVariatie1jewelry1v2 , productVariatie2jewelry1v2 , productVariatie3jewelry1v2 , productVariatie1jewelry2v1 , productVariatie2jewelry2v1 , productVariatie3jewelry2v1 , productVariatie1jewelry2v2 , productVariatie2jewelry2v2 , productVariatie3jewelry2v2 , productVariatie1jewelry3v1 , productVariatie2jewelry3v1 , productVariatie3jewelry3v1 , productVariatie1jewelry3v2 , productVariatie2jewelry3v2 , productVariatie3jewelry3v2 , productVariatie1bags1v1 , productVariatie2bags1v1 , productVariatie3bags1v1 , productVariatie1bags1v2 , productVariatie2bags1v2 ,
                productVariatie3bags1v2 , productVariatie1bags2v1 , productVariatie2bags2v1 , productVariatie3bags2v1 , productVariatie1bags2v2 , productVariatie2bags2v2 , productVariatie3bags2v2 , productVariatie1clothes1v1 , productVariatie2clothes1v1 , productVariatie3clothes1v1 , productVariatie4clothes1v1 , productVariatie5clothes1v1 , productVariatie6clothes1v1 , productVariatie1clothes1v2 , productVariatie2clothes1v2 , productVariatie3clothes1v2 , productVariatie4clothes1v2 , productVariatie5clothes1v2 , productVariatie6clothes1v2 , productVariatie1clothes2v1 , productVariatie2clothes2v1 , productVariatie3clothes2v1 , productVariatie4clothes2v1 , productVariatie5clothes2v1 , productVariatie6clothes2v1 , productVariatie1clothes2v2 , productVariatie2clothes2v2 , productVariatie3clothes2v2 , productVariatie4clothes2v2 , productVariatie5clothes2v2 , productVariatie6clothes2v2 ,
        };




        brandRepository.save(cartier);
        brandRepository.save(vacheron_constantin);
        brandRepository.save(tiffany_and_co);
        brandRepository.save(graff);
        brandRepository.save(chanel);
        brandRepository.save(hermes);
        brandRepository.save(gucci);

        colorRepository.save(black);
        colorRepository.save(white);
        colorRepository.save(silver);
        colorRepository.save(gold);
        colorRepository.save(red);
        colorRepository.save(green);
        colorRepository.save(brown);

        sizeRepository.save(xs);
        sizeRepository.save(s);
        sizeRepository.save(m);
        sizeRepository.save(l);
        sizeRepository.save(xl);
        sizeRepository.save(xxl);

        sizeAndFitRepository.save(classic);
        sizeAndFitRepository.save(regular);
        sizeAndFitRepository.save(relaxed);
        sizeAndFitRepository.save(slim);
        sizeAndFitRepository.save(loose);
        sizeAndFitRepository.save(tailord);

        productRepository.save(watch1);
        productRepository.save(watch2);
        productRepository.save(watch3);
        productRepository.save(jewelry1);
        productRepository.save(jewelry2);
        productRepository.save(jewelry3);
        productRepository.save(bags1);
        productRepository.save(bags2);
        productRepository.save(clothes1);
        productRepository.save(clothes2);

        productVariantRepository.saveAll(Arrays.asList(productVariants));
        productImagesRepository.saveAll(Arrays.asList(productImages));
        productVariatieRepository.saveAll(Arrays.asList(productVariaties));


    }

    private void seedUsers(){
        CustomUser customUser = new CustomUser();
        customUser.setName("Rami");
        customUser.setInfix("");
        customUser.setLastName("Al-Muhana");
        customUser.setEmail("test@mail.com");
        customUser.setPassword(new BCryptPasswordEncoder().encode("Test123!"));
        customUser.setRole("user");
        userRepository.save(customUser);

        CustomUser adminUser = new CustomUser();
        adminUser.setName("admin");
        adminUser.setInfix("");
        adminUser.setLastName("admin");
        adminUser.setEmail("admin@mail.com");
        adminUser.setPassword(new BCryptPasswordEncoder().encode("Test123!"));
        adminUser.setRole("admin");
        userRepository.save(adminUser);
    }

    private void seedPromoCodes() {
        Category watches = categoryRepository.findByName("Watches").orElse(null);
        Category jewelry = categoryRepository.findByName("Jewelry").orElse(null);
        Category bags = categoryRepository.findByName("Bags").orElse(null);
        Category clothes = categoryRepository.findByName("Clothes").orElse(null);

        LocalDateTime startDate = LocalDateTime.now();

        if (watches != null) {
            PromoCode fpsDiscount = new PromoCode("FPS_DISCOUNT", 20.0, LocalDateTime.of(2025, 5, 3, 2, 40, 1), startDate, 50, PromoCode.PromoCodeType.PERCENTAGE, watches, 5000);
            promoCodeRepository.save(fpsDiscount);
        }

        if (jewelry != null) {
            PromoCode actionDiscount = new PromoCode("ACTION_DISCOUNT", 150.0, LocalDateTime.of(2025, 6, 3, 2, 40, 1), startDate, 50, PromoCode.PromoCodeType.FIXED_AMOUNT, jewelry, 2000);
            promoCodeRepository.save(actionDiscount);
        }

        if (bags != null) {
            PromoCode creativeDiscount = new PromoCode("CREATIVE_DISCOUNT", 100.0, LocalDateTime.of(2025, 7, 3, 2, 40, 1), startDate, 50, PromoCode.PromoCodeType.FIXED_AMOUNT, bags, 4000);
            promoCodeRepository.save(creativeDiscount);
        }

        // todo : promocode voor Clothes category

        PromoCode promoCode1 = new PromoCode("SUMMER2024", 500, LocalDateTime.of(2024, 8, 31, 23, 59, 59), startDate, 100, PromoCode.PromoCodeType.FIXED_AMOUNT, null, 10000);
        PromoCode promoCode2 = new PromoCode("GAMER2024", 10, LocalDateTime.of(2024, 12, 31, 23, 59, 59), startDate, 50, PromoCode.PromoCodeType.PERCENTAGE, null, 4000);
        PromoCode promoCode3 = new PromoCode("GIFT2024", 20, LocalDateTime.of(2024, 11, 15, 23, 59, 59), startDate, 200, PromoCode.PromoCodeType.PERCENTAGE, null, 6000);
        PromoCode promoCode4 = new PromoCode("FIXED20", 200.0, LocalDateTime.of(2025, 5, 3, 2, 40, 1), startDate, 50, PromoCode.PromoCodeType.FIXED_AMOUNT, null, 2000);
        PromoCode promoCode5 = new PromoCode("AUTO_DISCOUNT", 500.0, LocalDateTime.of(2025, 5, 3, 2, 40, 1), startDate, 10000, PromoCode.PromoCodeType.FIXED_AMOUNT, null, 20000);

        promoCodeRepository.save(promoCode1);
        promoCodeRepository.save(promoCode2);
        promoCodeRepository.save(promoCode3);
        promoCodeRepository.save(promoCode4);
        promoCodeRepository.save(promoCode5);
    }
}
