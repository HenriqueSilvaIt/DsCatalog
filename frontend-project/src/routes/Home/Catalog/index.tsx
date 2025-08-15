
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
    categoryId: number
}

export default function Catalog() {

    const [totalPages, setTotalPages] = useState(0);

    const [isLastPage, setIsLastPage] = useState(false);

    const [products, setProducts] = useState<ProductDTO[]>([]); /* o tipo do useState vai ser uma lista de productDTO 
    por isso os [] para dizer que é lista de DTO, e no final ([]) para dizer que ela vai começar vazia
    , dentro do parentese () no useStat sempre é o valor iniciar no caso estamos colocando
[] para dizer que é uma lista vazia */

    const [queryParams, setQueryParams] = useState<QueryParams>({
        page: 0,
        name: "",
        categoryId: 0

    });

    function handleSearch(productName: string, categoryId: number) {
        setProducts([]); /* eu vou zerar a lista , para quando eu digitar ele 
        começar denovo na primeria página*/
        setQueryParams({ ...queryParams, page: 0, name: productName, categoryId: categoryId });
        console.log(queryParams.name);
    }

    useEffect(() => {

        productService.findPageRequest(queryParams.page, queryParams.name, 12, queryParams.categoryId, "name")
            .then(response => {
                const result = response.data.content;

                setProducts(result)

                setTotalPages(response.data.totalPages);
                setIsLastPage(response.data.last);
            })

    }, [queryParams]);


    function handlePageChange(page: number) {
        setProducts([]); // Zera a lista para uma nova requisição
        setQueryParams({ ...queryParams, page: page}); // backend começa
    }
    return (
        <>
            <main >
                <section id="catalog-section"className="dscatalog-catalog-container dscatalog-container">

                    <SearchCatalogBar onSearch={handleSearch} />

                    <div className="dscatalog-product-list">

                        {
                            products.map(product => <CatalogCard key={product.id} product={product} />
                            )

                        }


                    </div>

                    <PaginationCatalog
                        totalPages={totalPages}
                        currentPage={queryParams.page}
                        onPageChange={handlePageChange}
                        lastPage={isLastPage}
                    />
                </section>
            </main>
        </>
    );


}