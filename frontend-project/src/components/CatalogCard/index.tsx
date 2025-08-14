import './style.css';
import type { ProductDTO } from '../../models/product';

type Props = {
    product: ProductDTO;
}

export default function CatalogCard({product}: Props) {



    return (
        <>

            <div className="dscatalog-card-catalog-container ">
                <div className="dscatalog-card-catalog">
                    <img src={product.imgUrl} alt={product.name} />
                    <div className="dscatalog-card-catalog-details">
                        <h3>{product.name}</h3>
                        <div className="dscatalog-card-description">
                            <p>R$</p>
                            <h4> {product.price.toFixed(2)}</h4>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}