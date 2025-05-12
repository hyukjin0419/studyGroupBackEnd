/**
 * 스터디 그룹 관리 모듈
 * 스터디 목록 조회와 삭제 기능을 처리합니다.
 * 추가, 수정, 그룹원 관리 기능은 현재 백엔드 API가 구현되지 않아 지원하지 않습니다.
 */

// 스터디 그룹 관리 모듈
const studyModule = {
    studies: [],
    initialized: false,
    
    // 모듈 초기화
    async initStudies() {
        console.log('Initializing Studies Module');
        
        // 스터디 관리 UI 초기화
        this.initStudiesUI();
        
        // 스터디 목록 로드
        await this.loadStudies();
        
        // 이벤트 리스너 설정
        this.setupEventListeners();
        
        console.log('Studies Module initialized successfully');
    },
    
    // 스터디 관리 UI 초기화
    initStudiesUI() {
        const studiesContent = document.getElementById('studies-content');
        if (!studiesContent) {
            console.error('studies-content element not found');
            return;
        }
        
        // 스터디 관리 UI HTML - 백엔드 지원 기능만 포함 (목록 조회 및 삭제)
        studiesContent.innerHTML = `
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">스터디 그룹 목록</h5>
                    <div>
                        <small class="text-muted">추가/수정 기능은 현재 백엔드 API가 구현되지 않아 지원되지 않습니다</small>
                    </div>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>스터디명</th>
                                    <th>그룹장</th>
                                    <th>생성일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody id="studies-table-body">
                                <tr>
                                    <td colspan="5" class="text-center">로딩 중...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- 스터디 상세 모달 -->
            <div class="modal fade" id="studyDetailModal" tabindex="-1" aria-labelledby="studyDetailModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="studyDetailModalLabel">스터디 상세 정보</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body" id="study-detail-content">
                            <div class="text-center">
                                <div class="spinner-border" role="status">
                                    <span class="visually-hidden">로딩 중...</span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        console.log('Studies UI initialized with only supported features');
    },
    
    // 스터디 목록 로드
    async loadStudies() {
        console.log('Loading studies list...');
        
        try {
            // window.adminCore의 API_BASE_URL 값이 있는지 확인
            if (!window.adminCore || !window.adminCore.API_BASE_URL) {
                console.error('adminCore.API_BASE_URL이 정의되지 않았습니다');
                window.adminCore = window.adminCore || {};
                window.adminCore.API_BASE_URL = '/api/admin';
            }
            
            // members.js와 동일한 방식으로 API 호출
            const response = await axios.get(`${window.adminCore.API_BASE_URL}/groups`);
            console.log('Studies API response:', response.data);
            
            if (response.data.success) {
                this.studies = response.data.data || [];
                console.log(`Loaded ${this.studies.length} studies`);
                this.renderStudiesTable();
            } else {
                console.error('Failed to load studies:', response.data.message || 'Unknown error');
                this.handleStudiesLoadError('API 응답 오류');
            }
        } catch (error) {
            console.error('스터디 목록 로드 오류:', error);
            this.handleStudiesLoadError(error.message);
        }
    },
    
    // 로드 오류 처리
    handleStudiesLoadError(errorMessage) {
        const tableBody = document.getElementById('studies-table-body');
        if (tableBody) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-danger">
                        <i class="bi bi-exclamation-triangle me-2"></i>
                        스터디 목록을 불러오는 중 오류가 발생했습니다.
                        <small class="d-block mt-1">${errorMessage}</small>
                    </td>
                </tr>
            `;
        }
    },
    
    // 스터디 테이블 렌더링 - 백엔드 구현 기능만 포함
    renderStudiesTable() {
        console.log('Rendering studies table with only supported features...');
        const tableBody = document.getElementById('studies-table-body');
        
        if (!tableBody) {
            console.error('studies-table-body element not found');
            return;
        }
        
        if (!this.studies || this.studies.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center">
                        <i class="bi bi-info-circle me-2"></i>
                        등록된 스터디 그룹이 없습니다.
                    </td>
                </tr>
            `;
            return;
        }
        
        let html = '';
        
        this.studies.forEach(study => {
            // 생성일 처리
            const createdDate = study.createdAt || '';
            // 상태에 따른 배지 색상 처리
            let statusBadgeClass = 'bg-success';
            let statusText = '모집중';
            
            if (study.status === 'CLOSED') {
                statusBadgeClass = 'bg-warning';
                statusText = '모집완료';
            } else if (study.status === 'COMPLETED') {
                statusBadgeClass = 'bg-secondary';
                statusText = '완료';
            }
            
            html += `
                <tr>
                    <td>${study.id}</td>
                    <td>
                        <a href="javascript:void(0)" class="text-decoration-none view-study" data-id="${study.id}">
                            ${study.name}
                        </a>
                        <span class="badge ${statusBadgeClass} ms-2">${statusText}</span>
                    </td>
                    <td>${study.leader?.username || study.leader?.userName || '알 수 없음'}</td>
                    <td>${this.formatDate(createdDate)}</td>
                    <td>
                        <div class="btn-group btn-group-sm" role="group">
                            <button class="btn btn-sm btn-info view-study" data-id="${study.id}" title="스터디 상세 보기">
                                <i class="bi bi-eye"></i>
                            </button>
                            <button class="btn btn-sm btn-danger delete-study" data-id="${study.id}" title="스터디 삭제">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        });
        
        tableBody.innerHTML = html;
        console.log(`Studies table rendered with ${this.studies.length} rows`);
        
        // 테이블 버튼 리스너 추가
        this.addTableButtonListeners();
    },
    
    // 테이블 버튼 이벤트 리스너 추가 - 백엔드에서 지원하는 기능만
    addTableButtonListeners() {
        // 상세 보기 버튼 이벤트 리스너
        const viewButtons = document.querySelectorAll('.view-study');
        console.log(`Found ${viewButtons.length} view buttons`);
        
        viewButtons.forEach(button => {
            button.addEventListener('click', () => {
                const studyId = button.getAttribute('data-id');
                this.showStudyDetailModal(studyId);
            });
        });
        
        // 삭제 버튼 이벤트 리스너
        const deleteButtons = document.querySelectorAll('.delete-study');
        console.log(`Found ${deleteButtons.length} delete buttons`);
        
        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const studyId = button.getAttribute('data-id');
                this.confirmDeleteStudy(studyId);
            });
        });
    },
    
    // 이벤트 리스너 설정 - 백엔드 지원 기능만
    setupEventListeners() {
        console.log('Setting up study event listeners for supported features only');
        // 현재 백엔드 API에서 지원하는 기능만 이벤트 리스너 설정
    },
    
    // 스터디 추가 모달 표시
    async showAddStudyModal() {
        console.log('Showing add study modal');
        
        // 모달 타이틀 설정
        document.getElementById('studyModalLabel').textContent = '스터디 그룹 추가';
        
        // 폼 초기화
        document.getElementById('study-id').value = '';
        document.getElementById('study-name').value = '';
        document.getElementById('study-description').value = '';
        document.getElementById('study-max-members').value = '10';
        document.getElementById('study-status').value = 'OPEN';
        
        // 그룹장 선택 리스트 로드
        await this.loadUsersForSelect();
        
        // 모달 표시
        const modal = new bootstrap.Modal(document.getElementById('studyModal'));
        modal.show();
    },
    
    // 스터디 수정 모달 표시
    async showEditStudyModal(studyId) {
        console.log(`Showing edit modal for study ID: ${studyId}`);
        try {
            const study = this.studies.find(s => s.id == studyId);
            
            if (!study) {
                console.error(`ID가 ${studyId}인 스터디를 찾을 수 없습니다.`);
                window.adminCore.showModal('오류', `스터디 정보를 찾을 수 없습니다.`);
                return;
            }
            
            console.log('Study data for editing:', study);
            
            // 모달 타이틀 변경
            document.getElementById('studyModalLabel').textContent = '스터디 수정';
            
            // 폼 필드 값 설정
            document.getElementById('study-id').value = study.id;
            document.getElementById('study-name').value = study.name || '';
            document.getElementById('study-description').value = study.description || '';
            document.getElementById('study-max-members').value = study.maxMembers || 10;
            
            // 상태 선택
            const statusSelect = document.getElementById('study-status');
            if (statusSelect) {
                statusSelect.value = study.status || 'OPEN';
            }
            
            // 그룹장 목록 로드
            await this.loadUsersForSelect();
            
            // 그룹장 선택
            const leaderSelect = document.getElementById('study-leader');
            if (leaderSelect && study.leader && study.leader.id) {
                leaderSelect.value = study.leader.id;
            }
            
            // 그룹원 선택
            if (study.members && study.members.length > 0) {
                const membersSelect = document.getElementById('study-members');
                if (membersSelect) {
                    // multi-select의 값 선택
                    Array.from(membersSelect.options).forEach(option => {
                        // 만약 study.members에 이 멤버가 포함되어 있다면 선택
                        if (study.members.some(member => member.id == option.value)) {
                            option.selected = true;
                        }
                    });
                }
            }
            
            // 그룹원 정보가 없는 경우, 그룹원 정보를 따로 불러와보기
            if (!study.members) {
                try {
                    await this.loadStudyMembers(studyId);
                } catch (error) {
                    console.warn('스터디 그룹원 정보를 불러오는 중 오류:', error);
                    // 오류가 발생해도 계속 진행
                }
            }
            
            // 모달 표시
            const modalElement = document.getElementById('studyModal');
            if (modalElement) {
                const modal = new bootstrap.Modal(modalElement);
                modal.show();
                console.log('Study edit modal displayed');
            } else {
                console.error('studyModal element not found');
            }
        } catch (error) {
            console.error('스터디 정보 수정 모달 표시 오류:', error);
            window.adminCore.showModal('오류', `스터디 정보 수정 오류: ${error.message}`);
        }
    },
    
    // 특정 스터디의 멤버 정보 불러오기
    async loadStudyMembers(studyId) {
        console.log(`스터디 ID ${studyId}의 멤버 정보 로드 시도`);
        console.warn('이 API는 현재 백엔드에 구현되어 있지 않습니다');
        
        // 실제 API를 호출하지 않고 기본 회원 정보만 로드
        try {
            // 현재 study 객체에서 그룹장 정보만 화면에 표시
            const study = this.studies.find(s => s.id == studyId);
            if (study && study.leader) {
                console.log('현재 스터디 그룹장:', study.leader);
            }
        } catch (error) {
            console.error('스터디 멤버 정보 처리 오류:', error);
        }
    },
    
    // 사용자 선택 목록 로드
    async loadUsersForSelect() {
        try {
            // API 경로 구성
            const apiBaseUrl = window.adminCore && window.adminCore.API_BASE_URL ? 
                window.adminCore.API_BASE_URL : '/api';
            const apiPath = '/admin/users';
            
            const response = await axios.get(`${apiBaseUrl}${apiPath}`);
            if (response.data.success) {
                const users = response.data.data || [];
                const leaderSelect = document.getElementById('study-leader');
                const membersSelect = document.getElementById('study-members');
                
                if (users.length > 0) {
                    // 그룹장 선택 드롭다운 채우기
                    if (leaderSelect) {
                        let leaderOptions = `<option value="">그룹장을 선택하세요</option>`;
                        users.forEach(user => {
                            // 사용자명 필드명 처리 (userName 또는 username)
                            const userName = user.userName || user.username || user.name || user.email;
                            leaderOptions += `<option value="${user.id}">${userName} (${user.email})</option>`;
                        });
                        leaderSelect.innerHTML = leaderOptions;
                    }
                    
                    // 그룹원 선택 박스 채우기
                    if (membersSelect) {
                        let memberOptions = '';
                        users.forEach(user => {
                            const userName = user.userName || user.username || user.name || user.email;
                            memberOptions += `<option value="${user.id}">${userName} (${user.email})</option>`;
                        });
                        membersSelect.innerHTML = memberOptions;
                    }
                }
            }
        } catch (error) {
            console.error('사용자 목록 로드 오류:', error);
            window.adminCore.showModal('오류', `사용자 목록 로드 오류: ${error.message}`);
        }
    },
    
    // CSRF 헤더 가져오기
    getCsrfHeaders() {
        const headers = {};
        
        // 로그아웃 폼에서 CSRF 토큰 가져오기
        const logoutForm = document.querySelector('form[action*="logout"]');
        if (logoutForm) {
            const csrfInput = logoutForm.querySelector('input[name="_csrf"]');
            if (csrfInput) {
                headers['X-CSRF-TOKEN'] = csrfInput.value;
                console.log('Using CSRF token from logout form');
                return headers;
            }
        }
        
        // meta 태그에서 CSRF 토큰 가져오기
        const csrfMeta = document.querySelector('meta[name="_csrf"]');
        const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
        
        if (csrfMeta && csrfHeaderMeta) {
            const token = csrfMeta.getAttribute('content');
            const headerName = csrfHeaderMeta.getAttribute('content');
            headers[headerName] = token;
            console.log('Using CSRF token from meta tags');
            return headers;
        }
        
        console.warn('CSRF token not found');
        return headers;
    },
    
    // 스터디 저장 (추가 또는 수정)
    async saveStudy() {
        // 폼 유효성 검사
        const form = document.getElementById('study-form');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        // 폼 데이터 수집
        const studyId = document.getElementById('study-id').value;
        const name = document.getElementById('study-name').value.trim();
        const description = document.getElementById('study-description').value.trim();
        const leaderId = document.getElementById('study-leader').value;
        const maxMembers = document.getElementById('study-max-members').value || 10;
        const status = document.getElementById('study-status').value;
        
        // 그룹원 수집 (multi-select)
        const membersSelect = document.getElementById('study-members');
        const memberIds = [];
        
        if (membersSelect) {
            Array.from(membersSelect.selectedOptions).forEach(option => {
                memberIds.push(parseInt(option.value));
            });
        }
        
        // 스터디 데이터 객체 생성
        const studyData = {
            name: name,
            description: description,
            leaderId: leaderId,
            maxMembers: maxMembers,
            status: status,
            memberIds: memberIds // 그룹원 ID 목록 추가
        };
        
        console.log('서버로 보낼 스터디 데이터:', studyData);
        
        // 백엔드 API가 아직 구현되지 않았음을 알림
        window.adminCore.showModal('알림', '현재 백엔드에 스터디 그룹 추가/수정 API가 구현되어 있지 않습니다. 관리자에게 문의하세요.');
    },
    
    // 스터디 상세 모달 표시 - 백엔드가 제공하는 정보만 표시
    async showStudyDetailModal(studyId) {
        // 모달 찾기
        const studyDetailModal = document.getElementById('studyDetailModal');
        const studyDetailContent = document.getElementById('study-detail-content');
        
        if (!studyDetailModal || !studyDetailContent) {
            console.error('Study detail modal elements not found');
            return;
        }
        
        const study = this.studies.find(s => s.id == studyId);
        
        if (!study) {
            console.error(`Study with ID ${studyId} not found`);
            return;
        }
        
        // 모달 컬텐트 초기화
        studyDetailContent.innerHTML = `
            <div class="text-center">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">로딩 중...</span>
                </div>
            </div>
        `;
        
        // 모달 제목 업데이트
        const modalTitle = studyDetailModal.querySelector('.modal-title');
        if (modalTitle) {
            modalTitle.textContent = `스터디 상세: ${study.name}`;
        }
        
        // 모달 표시
        const modal = new bootstrap.Modal(studyDetailModal);
        modal.show();
        
        try {            
            // 상태에 따른 배지 색상 처리
            let statusBadgeClass = 'bg-success';
            let statusText = '모집중';
            
            if (study.status === 'CLOSED') {
                statusBadgeClass = 'bg-warning';
                statusText = '모집완료';
            } else if (study.status === 'COMPLETED') {
                statusBadgeClass = 'bg-secondary';
                statusText = '완료';
            }
            
            // 스터디 정보 - 백엔드에서 제공하는 정보만 표시
            const studyInfo = `
                <div class="alert alert-info" role="alert">
                    <i class="bi bi-info-circle me-2"></i>
                    그룹원 정보 가져오기 API가 현재 백엔드에 구현되지 않았습니다.
                </div>
                <h5>스터디 정보</h5>
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <h6 class="fw-bold">스터디명</h6>
                                <p>${study.name} <span class="badge ${statusBadgeClass}">${statusText}</span></p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <h6 class="fw-bold">그룹장</h6>
                                <p>${study.leader?.username || study.leader?.userName || '알 수 없음'}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <h6 class="fw-bold">생성일</h6>
                                <p>${this.formatDate(study.createdAt)}</p>
                            </div>
                            <div class="col-12 mb-3">
                                <h6 class="fw-bold">소개</h6>
                                <p>${study.description || '소개가 없습니다.'}</p>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            
            // 모달 컨텐트 업데이트
            studyDetailContent.innerHTML = studyInfo;
        } catch (error) {
            console.error('Error showing study detail modal:', error);
            studyDetailContent.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    스터디 정보를 불러오는 중 오류가 발생했습니다.
                </div>
            `;
        }
    },
    
    // 스터디 삭제 확인
    confirmDeleteStudy(studyId) {
        if (confirm('정말로 이 스터디 그룹을 삭제하시겠습니까? 이 작업은 취소할 수 없습니다.')) {
            this.deleteStudy(studyId);
        }
    },
    
    // 스터디 삭제 실행
    async deleteStudy(studyId) {
        try {
            // window.adminCore 확인
            if (!window.adminCore || !window.adminCore.API_BASE_URL) {
                console.error('adminCore.API_BASE_URL이 정의되지 않았습니다');
                window.adminCore = window.adminCore || {};
                window.adminCore.API_BASE_URL = '/api/admin';
            }
            
            // CSRF 헤더 준비
            const headers = this.getCsrfHeaders();
            
            console.log(`Deleting study at: ${window.adminCore.API_BASE_URL}/groups/${studyId}`);
            await axios.delete(`${window.adminCore.API_BASE_URL}/groups/${studyId}`, { headers });
            
            window.adminCore.showModal('알림', '스터디 그룹이 성공적으로 삭제되었습니다.');
            
            // 스터디 목록 다시 불러오기
            await this.loadStudies();
        } catch (error) {
            console.error('스터디 그룹 삭제 오류:', error);
            window.adminCore.showModal('오류', `스터디 그룹 삭제 중 오류가 발생했습니다: ${error.response?.data?.message || error.message}`);
        }
    },
    
    // 날짜 포맷 변환
    formatDate(dateString) {
        if (!dateString) return '-';
        
        try {
            const date = new Date(dateString);
            
            // 유효한 날짜인지 확인
            if (isNaN(date.getTime())) {
                return '-';
            }
            
            // 날짜 포맷팅
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        } catch (error) {
            console.error('날짜 포맷 오류:', error);
            return '-';
        }
    }
};

// 스터디 모듈을 전역 변수로 노출
window.studyModule = studyModule;

// 문서 로드 완료 시 스터디 모듈 초기화
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing Studies Module');
    // studies 페이지에서만 초기화
    if (window.location.pathname.includes('/admin/studies') || 
        document.getElementById('studies-content')) {
        studyModule.initStudies();
    }
});

// 탭 변경 시 스터디 모듈 초기화
document.addEventListener('show.bs.tab', function(event) {
    const targetId = event.target.getAttribute('data-bs-target');
    if (targetId === '#studies' || targetId === '#studies-tab-pane') {
        console.log('Studies tab selected, initializing');
        studyModule.initStudies();
    }
});
