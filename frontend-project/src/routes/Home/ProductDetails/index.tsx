import { useEffect, useState } from 'react';
import ProductDetailsCard from '../../../components/ProductDetailsCard';
import './style.css';
import type { ProductDTO } from '../../../models/product';
import * as productService from '../../../services/product-service';
import { useNavigate, useParams } from 'react-router-dom';

export default function ProductDetails() {

    const params = useParams();

    const [product, setProduct] = useState<ProductDTO>();

    const navigate = useNavigate();

    useEffect(() => {

        productService.findById(Number(params.productId))
            .then(response => {
                setProduct(response.data);
            }).catch((error) => {
                navigate("/");
                console.log("Produto não encontrado", error);
            })

    }, [])

    return (

        <main>

            <section id="product-deitals-section" className="dscatalog-container ">

                {product && <ProductDetailsCard  product={product}/>}

            </section>

        </main>
    );
}