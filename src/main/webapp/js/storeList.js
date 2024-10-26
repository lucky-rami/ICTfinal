
let pageNum = getParameterByName('pageNum') || 1; // URL에서 pageNum 추출, 기본값 1
let pageSize = getParameterByName('pageSize') || 12; // URL에서 pageSize 추출, 기본값 12
let category = getParameterByName('category');  // category 추가
let secondCategory = getParameterByName('second_category'); // second_category 추가
let currentFilterType = 'latest'; // 기본값 설정

// URL에서 특정 파라미터의 값을 가져오는 함수
function getParameterByName(name) {
    let url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

// 필터를 적용하는 함수
function applyFilter(category, second_category, pageNum = 1, pageSize = 12, filterType) {
 currentFilterType = filterType;
 currentPage = pageNum;

        // 하위 카테고리가 선택되면 상위 카테고리를 제거 (null로 설정)
        if (second_category) {
            category = null;  // 상위 카테고리를 초기화
        }

    // 카테고리와 페이지 정보를 함께 포함한 URL 생성
    let url = `/pagedProducts?pageNum=${pageNum}&pageSize=${pageSize}&filterType=${filterType}`;

    if (category) {
        url += `&category=${category}`;
    }
    if (second_category) {
        url += `&second_category=${second_category}`;
    }



    // AJAX 요청으로 서버에 필터 조건과 페이지 정보를 함께 전달
    $.ajax({
        url: url,
        method: 'GET',
        success: function(data) {
            // 성공 시 처리
            console.log("필터링된 상품 목록: ", data);
            updateProductList(data);  // 필터링된 상품 목록을 화면에 업데이트하는 함수 호출
        },
        error: function(error) {
            console.error("필터 적용 중 오류 발생: ", error);
        }
    });
}



//하위카테고리 적용 문제
document.addEventListener('DOMContentLoaded', function() {

    productList = document.querySelector('.list-carousel-images');
    if (!productList) {
        console.error("상품 목록 컨테이너를 찾을 수 없습니다.");
        return;
    }

// 필터링 조건이 있을 경우 파라미터에 추가
let url = `/pagedProducts?pageNum=${pageNum}&pageSize=${pageSize}`;
if (category) {
    url += `&category=${category}`;
}
if (secondCategory) {
    url += `&second_category=${secondCategory}`;
}

fetch(url)
    .then(response => response.json())
    .then(data => {
        console.log("Fetched data:", data); // 서버 응답 확인

        // data.pagedProducts가 배열인지 확인
        if (Array.isArray(data.pagedProducts)) {
            products = data.pagedProducts;  // 배열을 전역 변수에 할당

            // 기존 목록 초기화
            productList.innerHTML = '';

            // data.pagedProducts 배열을 순회하며 DOM 업데이트
            data.pagedProducts.forEach(product => {
                const listItem = document.createElement('li');
                listItem.className = 'list-product';
                listItem.setAttribute('data-date', product.date);
                listItem.setAttribute('data-popular', product.popularity);
                listItem.setAttribute('data-price', product.price);

                listItem.innerHTML = `
                    <a href="/storeDetail/${product.idx}">
                        <img src="http://192.168.1.180:8000/${product.thumImg}" alt="${product.title}">
                    </a>
                    <p>${product.title}</p>
                    <p>${product.price.toLocaleString()} 원</p>
                `;
                productList.appendChild(listItem);
            });

            // 기본 필터 적용 (예: 최신순)
            filterProductsByType(currentFilterType); // 현재 선택된 필터 적용
        } else {
            console.error("Expected an array, but received:", data.pagedProducts);
        }
    })
    .catch(error => console.error('Error:', error));
});


//여기서 작동
    // 모든 필터 버튼에서 'active' 클래스 제거
    let allFilters = document.querySelectorAll('.filter-options');
    allFilters.forEach(filter => filter.classList.remove('active'));

// 필터 적용 함수 정의
function filterProductsByType(filterType) {

    // 필터 타입에 따른 정렬을 시도하기 전에 products가 배열인지 확인
    if (!Array.isArray(products)) {
        console.error("products가 배열이 아니거나 정의되지 않았습니다.");
        return;
    }

    // 클릭된 필터 버튼에 'active' 클래스 추가
    const activeFilter = document.querySelector(`.filter-options[onclick="filterProductsByType('${filterType}')"]`);
    if (activeFilter) {
        activeFilter.classList.add('active');
    }


    if (filterType === 'latest') {
        products.sort((a, b) => new Date(b.regDT) - new Date(a.regDT));  // 최신순 정렬
    } else if (filterType === 'popular') {
        products.sort((a, b) => parseInt(b.likeCount) - parseInt(a.likeCount));  // 인기순 (좋아요 수) 정렬
    } else if (filterType === 'high-price') {
        products.sort((a, b) => parseInt(b.price) - parseInt(a.price));  // 높은 가격순 정렬
    } else if (filterType === 'low-price') {
        products.sort((a, b) => parseInt(a.price) - parseInt(b.price));  // 낮은 가격순 정렬
    }


    updateProductList(products);
}


    // 검색창의 입력 이벤트 리스너 추가
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('productSearch');
    if (searchInput) {
        searchInput.addEventListener('keyup', searchProducts); // 입력할 때마다 searchProducts 호출
    } else {
        console.error("검색 입력창을 찾을 수 없습니다.");
    }
});


//상품검색창
function searchProducts() {
    const input = document.getElementById('productSearch').value.toLowerCase();
    const productItems = document.querySelectorAll('.list-product'); // 상품 리스트

    productItems.forEach(item => {
        const productName = item.textContent.toLowerCase();
        if (productName.includes(input)) {
            item.style.display = ''; // 검색어가 포함된 항목 보이기
        } else {
            item.style.display = 'none'; // 검색어가 포함되지 않은 항목 숨기기
        }
    });
}

//이게 작동
function updateProductList(products) {
    console.log("updateProductList(products)=>",products);
    productList = document.querySelector('.list-carousel-images');
    if (!productList) {
        console.error("상품 목록 컨테이너를 찾을 수 없습니다.");
        return;  // productList가 정의되지 않았을 경우 함수 종료
    }

    productList.innerHTML = '';  // 기존의 상품 목록을 지움

    products.forEach(product => {
        const listItem = document.createElement('li');
        listItem.className = 'list-product';
        listItem.innerHTML = `
            <a href="/storeDetail/${product.idx}">
                <img src="http://192.168.1.180:8000/${product.thumImg}" alt="${product.title}">
            </a>
            <p>${product.title}</p>
            <p>${product.price.toLocaleString()} 원</p>
        `;
        productList.appendChild(listItem);  // 새로운 상품 목록을 추가
    });
}

////////////////////////////////////////

let subcategoryMap = {
      '아우터': 10,
      '상의': 11,
      '하의': 12,
      '잡화': 13,
      '아크릴': 20,
      '피규어': 21,
      '캔뱃지': 22,
      '슬로건': 23,
      '포스터': 24,
      '기타': 25,
      '필기류': 30,
      '노트&메모지': 31,
      '파일': 32,
      '스티커': 33,
      '달력': 34,
      '기타': 35,
      '컵&텀블러': 40,
      '쿠션': 41,
      '담요': 42,
      '기타': 43
  };


window.applyFilter = function(category, second_category, pageNum = 1) {
    const pageSize = 12;
    const secondCategoryValue = subcategoryMap[second_category] || null;

    const requestData = {
        pageNum: pageNum,
        pageSize: pageSize,
        category: category,
        second_category: secondCategoryValue
    };

    // URL 변경
    const newUrl = `/storeList?pageNum=${pageNum}&category=${category}`;
    history.pushState(null, '', newUrl);

    // AJAX 요청
    $.ajax({
        url: `/pagedProducts`,
        method: 'GET',
        data: requestData,
        success: function(data) {
            console.log("필터링된 상품 목록: ", data.pagedProducts);
            updateProductList(data.pagedProducts);

            // 페이지네이션 초기화, 현재 페이지를 인수로 전달
            const totalPages = Math.ceil(data.pagedProductscnt / pageSize);
            resetPaginationToFirstPage(totalPages, category, second_category, pageNum);
        },
        error: function(error) {
            console.error("필터 적용 중 오류 발생: ", error);
        }
    });
};


function resetPaginationToFirstPage(totalPages, category, second_category, currentPage) {
    const paginationElement = document.querySelector(".pagination");
    paginationElement.innerHTML = ''; // 기존 페이지네이션 요소 초기화

    // 이전 페이지 링크 추가 (첫 페이지가 아닐 경우에만 표시)
    if (currentPage > 1) {
        const prevPage = document.createElement('a');
        prevPage.textContent = '« 이전';
        prevPage.href = '#';
        prevPage.addEventListener('click', function(event) {
            event.preventDefault();
            applyFilter(category, second_category, currentPage - 1);
        });
        paginationElement.appendChild(prevPage);
    }

    // 페이지 번호 링크 생성
    // 시작 페이지와 끝 페이지 계산
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 1);

    for (let i = startPage; i <= endPage; i++) {
        const pageItem = document.createElement('a');
        pageItem.textContent = i;
        pageItem.href = '#';

        // 현재 페이지에 'current' 클래스 추가
        if (i === currentPage) {
            pageItem.classList.add('current');
        }

        // 페이지 클릭 이벤트 추가
        pageItem.addEventListener('click', function(event) {
            event.preventDefault();
            applyFilter(category, second_category, i); // 클릭한 페이지로 이동
        });

        paginationElement.appendChild(pageItem);
    }

    // 다음 페이지 링크 추가 (마지막 페이지가 아닐 경우에만 표시)
    if (currentPage < totalPages) {
        const nextPage = document.createElement('a');
        nextPage.textContent = '다음 »';
        nextPage.href = '#';
        nextPage.addEventListener('click', function(event) {
            event.preventDefault();
            applyFilter(category, second_category, currentPage + 1);
        });
        paginationElement.appendChild(nextPage);
    }
}








//지금까지
function showSubcategories(categoryId) {
    // subcategory-list 안의 모든 li.filter-item 요소를 가져옴
    const subcategories = document.querySelectorAll('#subcategory-list li.filter-item');
    console.log(subcategories); // 콘솔에서 li 목록 확인

    // AJAX 요청
    $.ajax({
        url: "/subcategories",
        type: "GET",
        data: { code: categoryId },
        success: (result) => {
        // AJAX 결과 확인

            // subcategories가 NodeList인지 확인 후 처리
            if (subcategories && subcategories.length > 0) {
                // 각 subcategory 요소에 대해 처리
                subcategories.forEach(subcategory => {
                    // subcategory의 텍스트와 AJAX로 받아온 값 비교
                    const subcategoryText = subcategory.textContent.trim(); // 공백 제거한 텍스트
                    if (result.includes(subcategoryText)) {
                        // 일치하는 경우 해당 항목을 숨김
                        subcategory.style.display = 'block';
                    } else {
                        // 일치하지 않으면 보이도록 처리
                        subcategory.style.display = 'none';
                    }
                });
            } else {
                console.error("No subcategories found.");
            }
        },
        error: (xhr, status, error) => {
            console.error("Error: " + error);
        }
    });
}

document.querySelectorAll('.category-filter a').forEach(function(categoryLink) {
    categoryLink.addEventListener('click', function(event) {
//        event.preventDefault(); // 기본 동작인 페이지 리로드를 막음

             let url = new URL(this.href);
             url.searchParams.set('pageNum', 1); // pageNum을 1로 설정

        // AJAX 요청을 통해 새로운 페이지 데이터를 가져옴
        fetch(url)
        .then(response => response.text())
        .then(html => {
            document.querySelector('#product-list').innerHTML = html; // 응답 데이터를 원하는 요소에 적용
        })
        .catch(error => {
            console.error('Error fetching category data:', error);
        });
    });
});
