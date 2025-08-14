
import CatalogCard from '../../../components/CatalogCard';
import PaginationCatalog from '../../../components/PaginationCatalog';
import SearchCatalogBar from '../../../components/SearchCatalogBar';
import './style.css';
import { useEffect, useState } from 'react';
import type { ProductDTO } from '../../../models/product';
import * as productService from '../../../services/product-service';

type QueryParams = {

    page: number;
    name: string;
}

export default function Catalog() {



    const [products, setProducts] = useState<ProductDTO[]>([]); /* o tipo do useState vai ser uma lista de productDTO 
    por isso os [] para dizer que é lista de DTO, e no final ([]) para dizer que ela vai começar vazia
    , dentro do parentese () no useStat sempre é o valor iniciar no caso estamos colocando
[] para dizer que é uma lista vazia */

    const [queryParams, setQueryParams ] = useState<QueryParams> ({
        page: 0,
        name: ""
        
    });

    function handleSearch(productName: string) {
        setProducts([]); /* eu vou zerar a lista , para quando eu digitar ele 
        começar denovo na primeria página*/
        setQueryParams({...queryParams, page: 0, name: productName});
        console.log(queryParams.name);
    }

    useEffect(() => {

        productService.findPageRequest(queryParams.page, queryParams.name)
            .then(response => {

                const nextPage = response.data.content;
                setProducts(products.concat(nextPage));
            })

    }, [queryParams]);

    return (
        <>
            <main >
                <section className="dscatalog-catalog-container dscatalog-container">

                    <SearchCatalogBar onSearch={handleSearch} />
                    <div className="dscatalog-product-list">
                        
                            {
                                products.map(product => <CatalogCard key={product.id} product={product} />
                                )

                            }
                    </div>

                    <PaginationCatalog />
                </section>
            </main>
        </>
    );


}